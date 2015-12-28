using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Threading;
using System.Runtime.CompilerServices;
using System.Collections;

namespace Facegraph_Savage
{
    class GraphApiContentSaver
    {
        private string url;
        private string fileName;
        private string id;
        private CommonResources common = CommonResources.getInstance();
        private FacebookRelatedStrings facebook = FacebookRelatedStrings.getInstance();

        private static Queue downloadQueue = Queue.Synchronized(new Queue());

        public static Queue DownloadQueue
        {
            get { return GraphApiContentSaver.downloadQueue; }
            set { GraphApiContentSaver.downloadQueue = value; }
        }

        private const int downloadInterval = 200;

        static GraphApiContentSaver()
        {
            Thread processingThread = new Thread(new ThreadStart(GraphApiContentSaver.processQueue));
            processingThread.IsBackground = true;
            processingThread.Start();
        }

        public GraphApiContentSaver(string id)
        {
            this.id = id;
            url = facebook.GraphApiUrl + id + "/";
            fileName = common.Path + "\\" + id + "\\" + "profile";
        }

        public void save()
        {

            WebClient client = new WebClient();
            Directory.CreateDirectory(Path.GetDirectoryName(fileName));
            try
            {
                client.DownloadFile(new Uri(url), fileName);
            }
            catch (Exception) 
            {
                enqueueDownloadProfileFile(id);
            }
        }


        private static void processQueue()
        {
            while (true)
            {
                if (downloadQueue.Count > 0)
                {
                    string id = (string)downloadQueue.Dequeue();
                    try
                    {
                        var saver = new GraphApiContentSaver(id);
                        Thread downThread = new Thread(new ThreadStart(saver.save));
                        downThread.IsBackground = true;
                        downThread.Start();
                    }
                    catch (Exception)
                    { }
                }
                Thread.Sleep(downloadInterval);
            }
        }

        public static void enqueueDownloadProfileFile(string id)
        {
            downloadQueue.Enqueue(id);
        }

    }
}
