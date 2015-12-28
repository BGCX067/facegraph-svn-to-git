namespace Facegraph_Savage
{
    partial class downloading
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
            this.label6 = new System.Windows.Forms.Label();
            this.completed = new System.Windows.Forms.ListView();
            this.completed_id = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.label5 = new System.Windows.Forms.Label();
            this.queue = new System.Windows.Forms.ListView();
            this.queue_id = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.label1 = new System.Windows.Forms.Label();
            this.totalProgress = new System.Windows.Forms.ProgressBar();
            this.totalProgressLabel = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.taskProgress = new System.Windows.Forms.ProgressBar();
            this.currentProfile = new System.Windows.Forms.Label();
            this.currentType = new System.Windows.Forms.Label();
            this.cancelDownload = new System.Windows.Forms.Button();
            this.label7 = new System.Windows.Forms.Label();
            this.estimated = new System.Windows.Forms.Label();
            this.queuedProfiles = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(241, 104);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(136, 13);
            this.label6.TabIndex = 21;

            // 
            // completed
            // 
            this.completed.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.completed_id});
            this.completed.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.completed.Location = new System.Drawing.Point(244, 129);
            this.completed.MultiSelect = false;
            this.completed.Name = "completed";
            this.completed.Size = new System.Drawing.Size(226, 252);
            this.completed.TabIndex = 20;
            this.completed.UseCompatibleStateImageBehavior = false;
            this.completed.View = System.Windows.Forms.View.Details;
            // 
            // completed_id
            // 
            this.completed_id.Text = "ID";
            this.completed_id.Width = 217;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(9, 104);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(104, 13);
            this.label5.TabIndex = 19;

            // 
            // queue
            // 
            this.queue.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.queue_id});
            this.queue.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.queue.Location = new System.Drawing.Point(12, 129);
            this.queue.MultiSelect = false;
            this.queue.Name = "queue";
            this.queue.Size = new System.Drawing.Size(226, 252);
            this.queue.TabIndex = 18;
            this.queue.UseCompatibleStateImageBehavior = false;
            this.queue.View = System.Windows.Forms.View.Details;
            // 
            // queue_id
            // 
            this.queue_id.Text = "ID";
            this.queue_id.Width = 218;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 391);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(92, 13);
            this.label1.TabIndex = 17;

            // 
            // totalProgress
            // 
            this.totalProgress.AccessibleDescription = "";
            this.totalProgress.AccessibleName = "";
            this.totalProgress.Location = new System.Drawing.Point(12, 407);
            this.totalProgress.Name = "totalProgress";
            this.totalProgress.Size = new System.Drawing.Size(458, 23);
            this.totalProgress.TabIndex = 15;
            // 
            // totalProgressLabel
            // 
            this.totalProgressLabel.Anchor = System.Windows.Forms.AnchorStyles.Top;
            this.totalProgressLabel.AutoSize = true;
            this.totalProgressLabel.Location = new System.Drawing.Point(110, 391);
            this.totalProgressLabel.Name = "totalProgressLabel";
            this.totalProgressLabel.Size = new System.Drawing.Size(24, 13);
            this.totalProgressLabel.TabIndex = 22;
            this.totalProgressLabel.Text = "0/0";
            this.totalProgressLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.label2.Location = new System.Drawing.Point(12, 9);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(174, 20);
            this.label2.TabIndex = 23;

            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(13, 45);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(33, 13);
            this.label3.TabIndex = 24;

            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(13, 72);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(28, 13);
            this.label4.TabIndex = 25;

            // 
            // taskProgress
            // 
            this.taskProgress.Location = new System.Drawing.Point(260, 65);
            this.taskProgress.Name = "taskProgress";
            this.taskProgress.Size = new System.Drawing.Size(200, 23);
            this.taskProgress.TabIndex = 26;
            // 
            // currentProfile
            // 
            this.currentProfile.AutoSize = true;
            this.currentProfile.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.currentProfile.Location = new System.Drawing.Point(52, 42);
            this.currentProfile.Name = "currentProfile";
            this.currentProfile.Size = new System.Drawing.Size(159, 20);
            this.currentProfile.TabIndex = 27;
            this.currentProfile.Text = "";
            // 
            // currentType
            // 
            this.currentType.AutoSize = true;
            this.currentType.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.currentType.Location = new System.Drawing.Point(52, 68);
            this.currentType.Name = "currentType";
            this.currentType.Size = new System.Drawing.Size(137, 20);
            this.currentType.TabIndex = 28;
            this.currentType.Text = "";
            // 
            // cancelDownload
            // 
            this.cancelDownload.Location = new System.Drawing.Point(309, 25);
            this.cancelDownload.Name = "cancelDownload";
            this.cancelDownload.Size = new System.Drawing.Size(103, 23);
            this.cancelDownload.TabIndex = 29;

            this.cancelDownload.UseVisualStyleBackColor = true;
            this.cancelDownload.Click += new System.EventHandler(this.cancelDownload_Click);
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(13, 447);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(90, 13);
            this.label7.TabIndex = 30;

            // 
            // estimated
            // 
            this.estimated.AutoSize = true;
            this.estimated.Location = new System.Drawing.Point(109, 447);
            this.estimated.Name = "estimated";
            this.estimated.Size = new System.Drawing.Size(74, 13);
            this.estimated.TabIndex = 31;

            // 
            // queuedProfiles
            // 
            this.queuedProfiles.AutoSize = true;
            this.queuedProfiles.Location = new System.Drawing.Point(369, 391);
            this.queuedProfiles.Name = "queuedProfiles";
            this.queuedProfiles.Size = new System.Drawing.Size(13, 13);
            this.queuedProfiles.TabIndex = 32;
            this.queuedProfiles.Text = "0";
            this.queuedProfiles.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(241, 391);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(122, 13);
            this.label8.TabIndex = 33;

            // 
            // downloading
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(482, 474);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.queuedProfiles);
            this.Controls.Add(this.estimated);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.cancelDownload);
            this.Controls.Add(this.currentType);
            this.Controls.Add(this.currentProfile);
            this.Controls.Add(this.taskProgress);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.totalProgressLabel);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.completed);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.queue);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.totalProgress);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "downloading";

            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.downloading_FormClosed);
            this.Load += new System.EventHandler(this.downloading_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.ListView completed;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.ListView queue;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ProgressBar totalProgress;
        private System.Windows.Forms.Label totalProgressLabel;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.ProgressBar taskProgress;
        private System.Windows.Forms.Label currentProfile;
        private System.Windows.Forms.Label currentType;
        private System.Windows.Forms.Button cancelDownload;
        private System.Windows.Forms.ColumnHeader completed_id;
        private System.Windows.Forms.ColumnHeader queue_id;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label estimated;
        private System.Windows.Forms.Label queuedProfiles;
        private System.Windows.Forms.Label label8;
    }
}