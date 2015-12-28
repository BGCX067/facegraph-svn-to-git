using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections;

namespace Facegraph_Savage
{
    public partial class Login : Form
    {
        private bool loggedIn = false;
        private GUIMessages messages = GUIMessages.getInstance();
        private bool canContinue = false;
        private Processing processing = null;
        private bool startingInstantly = false;
        private CommonResources common = CommonResources.getInstance();
        private FacebookRelatedStrings facebook = FacebookRelatedStrings.getInstance();

        public Login()
        {
            InitializeComponent();
            browse.Description = messages.SelectDirectory;
            toolTip.SetToolTip(startId, messages.StartIdToolTip);
            this.startButton.Text = messages.LoginFormStartButtonText;
            this.loggingLabel.Text = messages.LoginFormLoginToFacebookLabel;
            this.label3.Text = messages.LoginFormEmailLabel;
            this.label4.Text = messages.LoginFormPasswordLabel;
            this.label7.Text = messages.LoginFormStartProfileLabel;
            this.label8.Text = messages.LoginFormDepthLabel;
            this.label1.Text = messages.LoginFormPathLabel;
            this.Text = messages.LoginFormText;
        }

        public void setPath(string path)
        {
            common.Path = path;
            startingInstantly = true;
            this.path.Text = path;
            this.path.Enabled = false;
        }

        public void setDepth(string depth)
        {
            this.depth.Text = depth;
            this.depth.Enabled = false;
        }

        public void setStartingProfile(string profile)
        {
            this.startId.Text = profile;
            this.startId.Enabled = false;
        }
        
        private void Form1_Load(object sender, EventArgs e)
        {
            var splash = new Splash();
            splash.Show();
            mainWB.Navigate(facebook.Url);
            while(!loadAndCheckLogged())
                mainWB.Navigate(facebook.Url);
            splash.Hide();
            if (startingInstantly)
                instantStart();
        }

        private bool loadAndCheckLogged()
        {
            var loaded = false;
            while (!loaded)
            {
                if (mainWB.Document != null)
                    if (mainWB.Document.Body != null)
                        loaded = true;
                Application.DoEvents();
            }
            var document = mainWB.Document;
            var facebookElement = document.GetElementById("facebook");
            if (facebookElement == null)
            {
                DialogResult result = MessageBox.Show(this, messages.ConnectionError, messages.ConnectionError, MessageBoxButtons.RetryCancel, MessageBoxIcon.Error);
                if (result == DialogResult.Cancel)
                    Application.Exit();
                else return false;
            }
            
            var body = document.Body;
            loggedIn = !body.GetAttribute("className").Contains("LoggedOut");
            if (loggedIn)
            {
                email.Enabled = false;
                pass.Enabled = false;
                pass.Text = "";
                loggingLabel.Text = messages.UserLoggedIn;
            }
            return true;
        }

        private void login()
        {
            var doc = mainWB.Document;
            if (doc == null)
            {
                MessageBox.Show(messages.PageNotLoaded);
                return;
            }

            var emailElement = doc.GetElementById("email");
            var passElement = doc.GetElementById("pass");
            var clickElement = doc.GetElementById("loginbutton");
            if (emailElement == null || passElement == null || clickElement == null)
            {
                MessageBox.Show(messages.PageNotLoaded);
                return;
            }

            emailElement.SetAttribute("value", email.Text);
            passElement.SetAttribute("value", pass.Text);
            clickElement.InvokeMember("Click");

            var loaded = false;
            while (!loaded)
            {
                if (mainWB.Document != null)
                    if (mainWB.Document.GetElementById("pass") == null)
                        loaded = true;
                    else
                        if (mainWB.Document.GetElementById("pass").GetAttribute("value") != pass.Text)
                            loaded = true;
                Application.DoEvents();
            }
                    
            while(!loadAndCheckLogged())
                mainWB.Navigate(facebook.Url); ;

            if (!loggedIn)
            {
                mainWB.Navigate(facebook.Url);
                MessageBox.Show(messages.WrongUsername);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (path.Text == "")
            {
                MessageBox.Show(messages.ChoosePath);
                return;
            }
            if (!loggedIn)
            {
                if (email.Text == "" || pass.Text == "")
                {
                    MessageBox.Show(messages.FillEmail);
                    return;
                }
                login();
            }
            if (loggedIn)
            {
                if (!canContinue)
                {
                    if (startId.Text == "" || depth.Text == "")
                    {
                        MessageBox.Show(messages.FillDepth);
                        return;
                    }
                    processing = new Processing(startId.Text, Convert.ToInt32(depth.Text));
                }
                var form = new downloading();
                form.Show();
                this.Hide();
                mainWB.Dispose();
                processing.beginProcessing();
            }

        }

        private void browseButton_Click(object sender, EventArgs e)
        {
            browse.ShowDialog();
            path.Text = browse.SelectedPath;
            common.Path = path.Text;
            checkResume(false);
        }

        public void instantStart()
        {
            checkResume(true);
            if (common.Path != null && startId.Text != "")
                canContinue = true;
            if (canContinue)
                button1_Click(this, null);
        }

        private void checkResume(bool instantStart)
        {
            if (File.Exists(common.Path + common.StatusFileName))
            {
                Stream stream = File.Open(common.Path + common.StatusFileName, FileMode.Open);
                try
                {
                    Queue<string> userIdsToProcess = null;
                    ISet<string> usersWereInQueue = null;
                    ISet<string> downloadedPages = null;
                    Queue GapiQueue = null;
                    BinaryFormatter bFormatter = new BinaryFormatter();
                    userIdsToProcess = (Queue<string>)bFormatter.Deserialize(stream);
                    usersWereInQueue = (ISet<string>)bFormatter.Deserialize(stream);
                    downloadedPages = (ISet<string>)bFormatter.Deserialize(stream);
                    GapiQueue = (Queue)bFormatter.Deserialize(stream);
                    int currentDepth = (int)bFormatter.Deserialize(stream);
                    int nextDepthLevel = (int)bFormatter.Deserialize(stream);
                    int maxDepth = (int)bFormatter.Deserialize(stream);
                    this.processing = new Processing(userIdsToProcess, usersWereInQueue, downloadedPages, GapiQueue, currentDepth, nextDepthLevel, maxDepth);
                    DialogResult result = DialogResult.Yes;
                        if(!instantStart)
                            result = MessageBox.Show(this, messages.UnfinishedTasksFound, messages.UnfinishedTasksFound, MessageBoxButtons.YesNo, MessageBoxIcon.Question);
                    if (result == DialogResult.Yes)
                    {
                        startId.Enabled = false;
                        depth.Enabled = false;
                        canContinue = true;
                        path.Enabled = false;
                        browseButton.Enabled = false;
                    }
                }
                catch (Exception)
                {
                    MessageBox.Show(messages.StatusFileFail);
                }
                finally
                {
                    stream.Close();
                }
            }
        }

    }
}
