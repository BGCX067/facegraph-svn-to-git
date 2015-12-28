namespace Facegraph_Savage
{
    partial class Login
    {

        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.mainWB = new System.Windows.Forms.WebBrowser();
            this.startButton = new System.Windows.Forms.Button();
            this.email = new System.Windows.Forms.TextBox();
            this.loggingLabel = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.startId = new System.Windows.Forms.TextBox();
            this.label7 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.browse = new System.Windows.Forms.FolderBrowserDialog();
            this.pass = new System.Windows.Forms.TextBox();
            this.path = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.browseButton = new System.Windows.Forms.Button();
            this.depth = new System.Windows.Forms.MaskedTextBox();
            this.toolTip = new System.Windows.Forms.ToolTip(this.components);
            this.SuspendLayout();
            // 
            // mainWB
            // 
            this.mainWB.AllowWebBrowserDrop = false;
            this.mainWB.IsWebBrowserContextMenuEnabled = false;
            this.mainWB.Location = new System.Drawing.Point(422, 122);
            this.mainWB.MinimumSize = new System.Drawing.Size(20, 20);
            this.mainWB.Name = "mainWB";
            this.mainWB.Size = new System.Drawing.Size(43, 20);
            this.mainWB.TabIndex = 3;
            this.mainWB.TabStop = false;
            this.mainWB.Visible = false;
            this.mainWB.WebBrowserShortcutsEnabled = false;
            // 
            // startButton
            // 
            this.startButton.Location = new System.Drawing.Point(140, 122);
            this.startButton.Name = "startButton";
            this.startButton.Size = new System.Drawing.Size(218, 35);
            this.startButton.TabIndex = 4;

            this.startButton.UseVisualStyleBackColor = true;
            this.startButton.Click += new System.EventHandler(this.button1_Click);
            // 
            // email
            // 
            this.email.Location = new System.Drawing.Point(53, 25);
            this.email.Name = "email";
            this.email.Size = new System.Drawing.Size(152, 20);
            this.email.TabIndex = 5;
            // 
            // loggingLabel
            // 
            this.loggingLabel.AutoSize = true;
            this.loggingLabel.Location = new System.Drawing.Point(53, 9);
            this.loggingLabel.Name = "loggingLabel";
            this.loggingLabel.Size = new System.Drawing.Size(117, 13);
            this.loggingLabel.TabIndex = 6;

            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 28);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(35, 13);
            this.label3.TabIndex = 8;

            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(8, 54);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(39, 13);
            this.label4.TabIndex = 9;

            // 
            // startId
            // 
            this.startId.Location = new System.Drawing.Point(319, 25);
            this.startId.Name = "startId";
            this.startId.Size = new System.Drawing.Size(134, 20);
            this.startId.TabIndex = 15;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(220, 28);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(93, 13);
            this.label7.TabIndex = 16;

            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(220, 54);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(138, 13);
            this.label8.TabIndex = 18;

            // 
            // pass
            // 
            this.pass.Location = new System.Drawing.Point(53, 51);
            this.pass.Name = "pass";
            this.pass.Size = new System.Drawing.Size(152, 20);
            this.pass.TabIndex = 7;
            this.pass.UseSystemPasswordChar = true;
            // 
            // path
            // 
            this.path.Location = new System.Drawing.Point(95, 81);
            this.path.Name = "path";
            this.path.ReadOnly = true;
            this.path.Size = new System.Drawing.Size(334, 20);
            this.path.TabIndex = 19;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(8, 84);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(81, 13);
            this.label1.TabIndex = 20;

            // 
            // browseButton
            // 
            this.browseButton.Location = new System.Drawing.Point(429, 79);
            this.browseButton.Name = "browseButton";
            this.browseButton.Size = new System.Drawing.Size(24, 23);
            this.browseButton.TabIndex = 21;
            this.browseButton.Text = "...";
            this.browseButton.UseVisualStyleBackColor = true;
            this.browseButton.Click += new System.EventHandler(this.browseButton_Click);
            // 
            // depth
            // 
            this.depth.Location = new System.Drawing.Point(410, 51);
            this.depth.Mask = "0";
            this.depth.Name = "depth";
            this.depth.Size = new System.Drawing.Size(43, 20);
            this.depth.TabIndex = 22;
            this.depth.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            // 
            // Login
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(487, 169);
            this.Controls.Add(this.depth);
            this.Controls.Add(this.browseButton);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.path);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.startId);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.pass);
            this.Controls.Add(this.loggingLabel);
            this.Controls.Add(this.email);
            this.Controls.Add(this.startButton);
            this.Controls.Add(this.mainWB);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "Login";

            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.WebBrowser mainWB;
        private System.Windows.Forms.Button startButton;
        private System.Windows.Forms.TextBox email;
        private System.Windows.Forms.Label loggingLabel;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox startId;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.FolderBrowserDialog browse;
        private System.Windows.Forms.TextBox pass;
        private System.Windows.Forms.TextBox path;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button browseButton;
        private System.Windows.Forms.MaskedTextBox depth;
        private System.Windows.Forms.ToolTip toolTip;
    }
}