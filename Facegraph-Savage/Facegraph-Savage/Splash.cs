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
    public partial class Splash : Form
    {
        public Splash()
        {
            InitializeComponent();
        }

        public void changeMessage(string message)
        {
            this.message.Text = message;
            this.Text = message;
        }

        private void Splash_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
