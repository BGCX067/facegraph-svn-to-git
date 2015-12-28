using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    public class GUIMessages
    {
        private static GUIMessages _instance = null;
        private GUIMessages() { }
        public static GUIMessages getInstance()
        {
            if (_instance == null)
                _instance = new GUIMessages();
            return _instance;
        }
        private readonly string selectDirectory = "Wskaż folder zapisu:";
        private readonly string connectionError = "Blad polaczenia z internetem!";
        private readonly string pageNotLoaded = "Nie załadowano strony logowania!";
        private readonly string userLoggedIn = "Użytkownik zalogowany!";
        private readonly string wrongUsername = "Błędny login lub hasło.";
        private readonly string friendList = "Lista znajomych";
        private readonly string favorites = "Ulubione";
        private readonly string unfinishedTasksFound = "Znaleziono nieukończone zadania. Czy chcesz wznowić wcześniejsze pobieranie?";
        private readonly string statusFileFail = "Nie udało się odczytać pliku statusu.";
        private readonly string choosePath = "Wybierz ścieżkę.";
        private readonly string fillEmail = "Uzupełnij email i hasło.";
        private readonly string fillDepth = "Wybierz profil początkowy i głębokość przeszukiwania.";
        private readonly string startIdToolTip = "Numer identyfikacyjny użytkownika od którego program rozpocznie przeszukiwanie.";
        private readonly string downloadingFinished = "Zakończono pobieranie";

        public string DownloadingFinished
        {
            get { return downloadingFinished; }
        } 

        private readonly string loginFormText = "Rozpocznij pobieranie";

        public string LoginFormText
        {
            get { return loginFormText; }
        }

        private readonly string loginFormLoginToFacebookLabel = "Zaloguj do Facebooka:";

        public string LoginFormLoginToFacebookLabel
        {
            get { return loginFormLoginToFacebookLabel; }
        }

        private readonly string loginFormEmailLabel = "Email:";

        public string LoginFormEmailLabel
        {
            get { return loginFormEmailLabel; }
        }

        private readonly string loginFormPasswordLabel = "Hasło:";

        public string LoginFormPasswordLabel
        {
            get { return loginFormPasswordLabel; }
        }

        private readonly string loginFormStartButtonText = "Rozpocznij";

        public string LoginFormStartButtonText
        {
            get { return loginFormStartButtonText; }
        }

        private readonly string loginFormStartProfileLabel = "Profil początkowy:";

        public string LoginFormStartProfileLabel
        {
            get { return loginFormStartProfileLabel; }
        }

        private readonly string loginFormDepthLabel = "Głębokość przeszukiwania:";

        public string LoginFormDepthLabel
        {
            get { return loginFormDepthLabel; }
        }

        private readonly string loginFormPathLabel = "Ścieżka zapisu:";

        public string LoginFormPathLabel
        {
            get { return loginFormPathLabel; }
        }


        public string StartIdToolTip
        {
            get { return startIdToolTip; }
        }

        public string FillDepth
        {
            get { return fillDepth; }
        }

        public string FillEmail
        {
            get { return fillEmail; }
        }

        public string ChoosePath
        {
            get { return choosePath; }
        }

        public string StatusFileFail
        {
            get { return statusFileFail; }
        }

        public string UnfinishedTasksFound
        {
            get { return unfinishedTasksFound; }
        }

        public string Favorites
        {
            get { return favorites; }
        }

        public string FriendList
        {
            get { return friendList; }
        }

        public string WrongUsername
        {
            get { return wrongUsername; }
        }

        public string ConnectionError
        {
            get { return connectionError; }
        }

        public string UserLoggedIn
        {
            get { return userLoggedIn; }
        }

        public string PageNotLoaded
        {
            get { return pageNotLoaded; }
        }

        public string SelectDirectory
        {
            get { return selectDirectory; }
        }

    }
}
