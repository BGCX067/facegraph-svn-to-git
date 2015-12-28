using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    class DownloadingFormMessages
    {
        private static DownloadingFormMessages _instance = null;
        private DownloadingFormMessages() { }
        public static DownloadingFormMessages getInstance()
        {
            if (_instance == null)
                _instance = new DownloadingFormMessages();
            return _instance;
        }

        private readonly string finishing = "Kończenie pracy";

        public string Finishing
        {
            get { return finishing; }  
        } 

        private readonly string downloadingEndsAfterCurrentElement = "Pobieranie zostanie zakończone po bieżącym elemencie...";

        public string DownloadingEndsAfterCurrentElement
        {
            get { return downloadingEndsAfterCurrentElement; }
        } 

        private readonly string closing = "Zamykanie...";

        public string Closing
        {
            get { return closing; }
        } 

        private readonly string currentDownload = "Aktualnie pobierane:";

        public string CurrentDownload
        {
            get { return currentDownload; }
        }

        private readonly string barText = "Pobieranie";

        public string BarText
        {
            get { return barText; }
        }

        private readonly string profileLabel = "Profil:";

        public string ProfileLabel
        {
            get { return profileLabel; }
        }

        private readonly string typeLabel = "Typ:";

        public string TypeLabel
        {
            get { return typeLabel; }
        }

        private readonly string cancelDownloadingButton = "Przerwij pobieranie";

        public string CancelDownloadingButton
        {
            get { return cancelDownloadingButton; }
        }

        private readonly string queueLabel = "Następne w kolejce:";

        public string QueueLabel
        {
            get { return queueLabel; }
        }

        private readonly string finishedDownsLabel = "Ostatnio pobrane elementy:";

        public string FinishedDownsLabel
        {
            get { return finishedDownsLabel; }
        }

        private readonly string totalProgressLabel = "Postęp całkowity:";

        public string TotalProgressLabel
        {
            get { return totalProgressLabel; }
        }

        private readonly string estimatedTimeLabel = "Szacowany czas:";

        public string EstimatedTimeLabel
        {
            get { return estimatedTimeLabel; }
        }

        private readonly string estimatingLabel = "Szacowanie...";

        public string EstimatingLabel
        {
            get { return estimatingLabel; }
        }

        private readonly string profileFilesLabel = "Pozostało plików profilu:";

        public string ProfileFilesLabel
        {
            get { return profileFilesLabel; }
        } 


    }
}
