using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Facegraph_Savage
{
    public partial class downloading : Form
    {
        private DownloadingFormMessages messages = DownloadingFormMessages.getInstance();

        public downloading()
        {
            InitializeComponent();
            this.label6.Text = messages.FinishedDownsLabel;
            this.label5.Text = messages.QueueLabel;
            this.label1.Text = messages.TotalProgressLabel;
            this.label2.Text = messages.CurrentDownload;
            this.label3.Text = messages.ProfileLabel;
            this.label4.Text = messages.TypeLabel;
            this.cancelDownload.Text = messages.CancelDownloadingButton;
            this.label7.Text = messages.EstimatedTimeLabel;
            this.estimated.Text = messages.EstimatingLabel;
            this.label8.Text = messages.ProfileFilesLabel;
            this.Text = messages.BarText;
        }

        private void downloading_Load(object sender, EventArgs e)
        {
            var pListener = ProgressListener.getInstance();
            pListener.TaskProgressBar = taskProgress;
            pListener.QueueLV = queue;
            pListener.TaskProfileId = currentProfile;
            pListener.TaskTypeLabel = currentType;
            pListener.TotalProgressBar = totalProgress;
            pListener.TotalProgressLabel = totalProgressLabel;
            pListener.DoneLV = completed;
            pListener.EstimatedLabel = estimated;
            pListener.QueuedProfiles = queuedProfiles;
        }

        private void cancelDownload_Click(object sender, EventArgs e)
        {
            Processing.abort();
            cancelDownload.Enabled = false;
            cancelDownload.Text = messages.Closing;
            estimated.Text = messages.DownloadingEndsAfterCurrentElement;
        }

        private void downloading_FormClosed(object sender, FormClosedEventArgs e)
        {
            this.Hide();
            if (Processing.IsRunning)
            {
                Processing.abort();
                Splash info = new Splash();
                info.changeMessage(messages.Finishing);
                info.Show();
            }
            else Application.Exit();
        }
    }
}
