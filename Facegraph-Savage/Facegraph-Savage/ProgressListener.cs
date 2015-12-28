using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Collections;
using System.IO;

namespace Facegraph_Savage
{
    class ProgressListener
    {
        private static ProgressListener _instance = null;
        private CommonResources common = CommonResources.getInstance();
        private GUIMessages messages = GUIMessages.getInstance();
        private ProgressListener()
        {
        }

        public static ProgressListener getInstance()
        {
            if (_instance == null)
                _instance = new ProgressListener();
            return _instance;
        }

        private ProgressBar progressBar;
        private Label taskTypeLabel;
        private Label taskProfileId;
        private Label totalProgressLabel;
        private ProgressBar totalProgressBar;
        private ListView queueLV;
        private ListView doneLV;
        private Label estimatedLabel;
        private Label queuedProfiles;

        public Label QueuedProfiles
        {
            set { queuedProfiles = value; }
        }

        public Label EstimatedLabel
        {
            set { estimatedLabel = value; }
        }

        public ListView DoneLV
        {
            set { doneLV = value; }
        }

        public ListView QueueLV
        {
            set { queueLV = value; }
        }

        public ProgressBar TotalProgressBar
        {
            set { totalProgressBar = value; }
        }

        public Label TotalProgressLabel
        {
            set { totalProgressLabel = value; }
        }

        public Label TaskProfileId
        {
            set { taskProfileId = value; }
        }

        public Label TaskTypeLabel
        {
            set { taskTypeLabel = value; }
        }

        public ProgressBar TaskProgressBar
        {
            set {this.progressBar = value;}
        }

        public void startReportingTaskProgress(int maxValue)
        {
            progressBar.Maximum = maxValue;
            progressBar.Value = 0;
        }

        public void reportTaskProgress(int currentValue)
        {
            if (currentValue<=progressBar.Maximum)
                progressBar.Value = currentValue;
        }

        public void setTaskInfo(string profileId, string taskType)
        {
            taskTypeLabel.Text = taskType;
            taskProfileId.Text = profileId;
        }

        public void reportQueueChange(int done, int total, ref Queue<string> queue)
        {
            totalProgressLabel.Text = Convert.ToString(done) + "/" + Convert.ToString(total);
            totalProgressBar.Maximum = total;
            totalProgressBar.Value = done;
            queueLV.Items.Clear();
            for (int i = 0; i < queue.Count && i < 10; i++)
            {
                string task = queue.ElementAt(i);
                queueLV.Items.Add(task);
            }
        }

        public void reportTaskDone(string id, string estimatedTime)
        {
            doneLV.Items.Add(id);
            if (doneLV.Items.Count > 10)
                doneLV.Items.RemoveAt(0);
            estimatedLabel.Text = estimatedTime;
            queuedProfiles.Text = Convert.ToString(GraphApiContentSaver.DownloadQueue.Count);
        }

        public void reportFinnish() 
        {
            taskTypeLabel.Text = "";
            taskProfileId.Text = "";
            totalProgressLabel.Text = "";
            if (File.Exists(common.Path + common.StatusFileName))
            {
                File.Delete(common.Path + common.StatusFileName);
            }
            totalProgressBar.Value = totalProgressBar.Maximum;
            MessageBox.Show(messages.DownloadingFinished);
        }

        public void reportQueuedProfiles()
        {
            queuedProfiles.Text = Convert.ToString(GraphApiContentSaver.DownloadQueue.Count);
        }

    }
}
