using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections;
using System.Runtime.InteropServices;

namespace Facegraph_Savage
{
    class Processing
    {
        private CommonResources common = CommonResources.getInstance();
        private Queue<string> userIdsToProcess = new Queue<string>();
        private ISet<string> usersWereInQueue = new HashSet<string>();
        private ISet<string> downloadedPages = new HashSet<string>();
        private IList<long> taskTime = new List<long>();
        private ProgressListener progressListener = ProgressListener.getInstance();
        private GUIMessages messages = GUIMessages.getInstance();
        private static bool aborted = false;
        private bool enqueueNew = true;
        private int currentDepth = 0;
        private int nextDepthLevel = 0;
        private int maxDepth = 0;
        private int singleProcessProfiles = 0;
        private const int maxSingleProcessProfiles = 30;

        [DllImport("kernel32.dll")]
        static extern bool SetProcessWorkingSetSize(IntPtr handle, int minSize, int maxSize);


        public Processing(string firstId, int maxDepth)
        {
            userIdsToProcess.Enqueue(firstId);
            usersWereInQueue.Add(firstId);
            this.maxDepth = maxDepth;
        }

        public Processing(Queue<string> userIdsToProcess, ISet<string> usersWereInQueue, ISet<string> downloadedPages, Queue GapiQueue, int currentDepth, int nextDepthLevel, int maxDepth)
        {
            this.userIdsToProcess = userIdsToProcess;
            this.usersWereInQueue = usersWereInQueue;
            this.downloadedPages = downloadedPages;
            GraphApiContentSaver.DownloadQueue = GapiQueue;
            this.currentDepth = currentDepth;
            this.nextDepthLevel = nextDepthLevel;
            this.maxDepth = maxDepth;
        }

        private void downloadProfileFiles(ref ISet<string> idSet)
        {
            foreach (var id in idSet)
            {
                GraphApiContentSaver.enqueueDownloadProfileFile(id);
            }
        }

        private string computeEstimatedTime()
        {
            double avg = taskTime.Average();
            long estimatedMillis = (long)(avg * userIdsToProcess.Count);
            TimeSpan estimatedTimeSpan = TimeSpan.FromMilliseconds(estimatedMillis);
            return estimatedTimeSpan.Days > 0 ? estimatedTimeSpan.ToString("%d' d. '%h' godz. '%m' min.'") : estimatedTimeSpan.ToString("%h' godz. '%m' min.'");
        }

        public static bool IsRunning
        {
            get;
            set;
        }

        public void beginProcessing()
        {
            IsRunning = true;
            while (userIdsToProcess.Count != 0 && !aborted)
            {
                if (currentDepth == maxDepth)
                    breakEnqueueNew();

                Stopwatch swatch = new Stopwatch();
                swatch.Start();
                var idToProcess = userIdsToProcess.Dequeue();
                progressListener.setTaskInfo(idToProcess, messages.FriendList);
                progressListener.reportQueueChange(usersWereInQueue.Count - userIdsToProcess.Count, usersWereInQueue.Count, ref userIdsToProcess);
                SourceContentSaver friendsSaver = new FriendsSourceContentSaver(idToProcess);
                var friendList = friendsSaver.saveFileAndGetIds();
                friendsSaver = null;
                friendList.ExceptWith(usersWereInQueue);


                if (enqueueNew)
                {
                    usersWereInQueue.UnionWith(friendList);
                    foreach (var userId in friendList)
                        userIdsToProcess.Enqueue(userId);
                }

                friendList = null;

                //downloadProfileFiles(friendList);
                GraphApiContentSaver.enqueueDownloadProfileFile(idToProcess);

                progressListener.setTaskInfo(idToProcess, messages.Favorites);
                SourceContentSaver likesSaver = new LikesSourceContentSaver(idToProcess);
                var likesList = likesSaver.saveFileAndGetIds();
                likesSaver = null;

                likesList.ExceptWith(downloadedPages);
                downloadedPages.UnionWith(likesList);

                downloadProfileFiles(ref likesList);

                likesList = null;

                swatch.Stop();
                taskTime.Add(swatch.ElapsedMilliseconds);
                string estimatedTime = computeEstimatedTime();
                progressListener.reportTaskDone(idToProcess, estimatedTime);
                int downloadedCount = usersWereInQueue.Count - userIdsToProcess.Count;
                if (downloadedCount >= nextDepthLevel)
                {
                    nextDepthLevel = usersWereInQueue.Count;
                    currentDepth++;
                }
                saveWorkStatus();

                if (singleProcessProfiles < maxSingleProcessProfiles)
                    singleProcessProfiles++;
                else
                    restartApplication();

                SetProcessWorkingSetSize(Process.GetCurrentProcess().Handle,-1,-1);

            }
            IsRunning = false;
            if (aborted) Application.Exit();
            else
            {
                waitForProfiles();
                progressListener.reportFinnish();
            }
        }

        private void waitForProfiles()
        {
            while (GraphApiContentSaver.DownloadQueue.Count != 0 && !aborted)
            {
                progressListener.reportQueuedProfiles();
                Application.DoEvents();
            }
            if (aborted)
            {
                saveWorkStatus();
                Application.Exit();
            }
        }

        private void saveWorkStatus()
        {
            if (File.Exists(common.Path + common.StatusFileName))
                File.Copy(common.Path + common.StatusFileName, common.Path + common.BackupStatusFileName, true);
            Stream stream = File.Open(common.Path + common.StatusFileName, FileMode.Create);
            BinaryFormatter bFormatter = new BinaryFormatter();
            bFormatter.Serialize(stream, userIdsToProcess);
            bFormatter.Serialize(stream, usersWereInQueue);
            bFormatter.Serialize(stream, downloadedPages);
            bFormatter.Serialize(stream, GraphApiContentSaver.DownloadQueue);
            bFormatter.Serialize(stream, currentDepth);
            bFormatter.Serialize(stream, nextDepthLevel);
            bFormatter.Serialize(stream, maxDepth);
            stream.Close();
        }

        public static void abort()
        {
            aborted = true;
        }

        public void breakEnqueueNew()
        {
            enqueueNew = false;
        }

        void restartApplication()
        {
            Process copy = new Process();
            copy.StartInfo.FileName = Application.ExecutablePath;
            copy.StartInfo.Arguments = "-p \"" + common.Path + '"';
            copy.Start();
            Application.Exit();
        }


    }
}
