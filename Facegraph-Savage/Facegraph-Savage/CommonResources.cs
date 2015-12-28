using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    class CommonResources
    {
        private static CommonResources _instance = null;
        private CommonResources() { }

        public static CommonResources getInstance()
        {
            if (_instance == null)
                _instance = new CommonResources();
            return _instance;
        }

        private string path;

        public string Path
        {
            get { return path; }
            set { path = value; }
        }

        private readonly string statusFileName = "\\status.dat";
        

        public string StatusFileName
        {
            get { return statusFileName; }
        }

        private readonly string backupStatusFileName = "\\status_backup.dat";

        public string BackupStatusFileName
        {
            get { return backupStatusFileName; }
        } 


    }
}
