using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Diagnostics;


namespace Facegraph_Savage
{
    class FriendsContentManager : IContentManager
    {
        private WebBrowser _webBrowser;
        private int _friendsCount;
        private string _friendsCountHeader;
        private ProgressListener _progress;

        private WebBrowser webBrowser
        {
            get { return _webBrowser; }
            set { _webBrowser = value; }
        }

        public FriendsContentManager(WebBrowser webBrowser, Layouts layout)
        {
            switch (layout)
            {
                case Layouts.Timeline:
                    _friendsCountHeader = "H3";
                    break;
                case Layouts.NoTimeline:
                    _friendsCountHeader = "H5";
                    break;
            }
            this.webBrowser = webBrowser;
        }

        public void loadDynamicContent()
        {
            bool loaded = false;
            string friendsCountLine = "";

            var now = DateTime.Now;
            var maxLoadingTime = now.AddSeconds(5);
            while (!loaded && DateTime.Now < maxLoadingTime)
            {
                Application.DoEvents();

                HtmlDocument doc = webBrowser.Document;
                if (doc != null)
                {
                    var headers = doc.GetElementsByTagName(_friendsCountHeader);

                    foreach (HtmlElement header in headers)
                    {
                        if (header.InnerText.Contains("Znajomi"))
                        {
                            friendsCountLine = header.InnerText;
                            loaded = true;
                            break;
                        }
                    }
                    headers = null;
                }
                doc = null;
            }
            if (!loaded)
            {
                this._friendsCount = 0;
                return;
            }
            Regex r = new Regex("(?<=\\().+?(?=\\))");
            Match m = r.Match(friendsCountLine);
            if (m.Success)
                friendsCountLine = m.Value;
            friendsCountLine = friendsCountLine.Replace(".", "");
            int friendsCount = Convert.ToInt32(friendsCountLine);
            this._friendsCount = friendsCount;
            
        }

        public ISet<string> getContent()
        {
            _progress = ProgressListener.getInstance();
            _progress.startReportingTaskProgress(_friendsCount);
            
            ISet<string> userIds = new HashSet<string>();

            var now = DateTime.Now;
            var maxWaitingTime = now.AddMinutes(1);
            while ((userIds.Count < _friendsCount - 2) && DateTime.Now < maxWaitingTime)
            {
                HtmlDocument document = webBrowser.Document;
                var links = document.Links;
                foreach (HtmlElement link in links)
                {
                    string id = fetchFriendId(link);
                    if (id!=null)
                        userIds.Add(id);
                }   
                document = null;
                links = null;
                _progress.reportTaskProgress(userIds.Count);
                Application.DoEvents();
            }
            //_progress.finishReporting(this);
            return userIds;
        }
        private string fetchFriendId(HtmlElement link)
        {
            string id = null;
            string dataHovercard = link.GetAttribute("data-hovercard");
            if (dataHovercard.Contains("user.php")
                && link.GetAttribute("data-gt") == "")
            {
                id = dataHovercard.Substring(28);
            }
            return id;
        }
    }
}
