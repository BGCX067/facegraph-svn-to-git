using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.Text.RegularExpressions;

namespace Facegraph_Savage
{
    class LikesContentManager : IContentManager
    {
        private WebBrowser _webBrowser;
        private ProgressListener _progress;

        private WebBrowser webBrowser
        {
            get { return _webBrowser; }
            set { _webBrowser = value; }
        }

        public LikesContentManager(WebBrowser webBrowser)
        {
            this.webBrowser = webBrowser;
        }

        public void loadDynamicContent()
        {
        }

        public ISet<string> getContent()
        {
            _progress = ProgressListener.getInstance();
            ISet<string> userIds = new HashSet<string>();

            Stopwatch watch = new Stopwatch();
            watch.Start();
            int lastChange = 0;
            int lastCount = 0;
            int millisWithoutChange = 0;
            int maxMillisWithoutChange = 3000;
            Regex friendIdPattern = new Regex("(?<=(page|application)\\.php\\?id\\=)\\d+");

            _progress.startReportingTaskProgress(maxMillisWithoutChange);
            while (millisWithoutChange < maxMillisWithoutChange)
            {
                HtmlDocument document2 = webBrowser.Document;
                var links = document2.Links;
                foreach (HtmlElement link in links)
                {
                    addIfIdOrClickIfDynamicLink(link, userIds, friendIdPattern);
                }

                var inputs = document2.GetElementsByTagName("input");
                foreach (HtmlElement input in inputs)
                {
                    var profileid = input.GetAttribute("data-profileid");
                    if (profileid.Length != 0)
                        userIds.Add(profileid);
                }
                if (userIds.Count > lastCount)
                {
                    lastCount = userIds.Count;
                    lastChange = watch.Elapsed.Seconds*1000 + watch.Elapsed.Milliseconds;
                }
                document2 = null;
                inputs = null;
                Application.DoEvents();
                millisWithoutChange = watch.Elapsed.Seconds * 1000 + watch.Elapsed.Milliseconds - lastChange;
                if (millisWithoutChange <= maxMillisWithoutChange)
                    _progress.reportTaskProgress(millisWithoutChange);
                else
                    _progress.reportTaskProgress(maxMillisWithoutChange);
            }
            watch.Stop();
            watch = null;
            //_progress.finishReporting(this);
            return userIds;
        }
        private void addIfIdOrClickIfDynamicLink(HtmlElement link, ISet<string> userIds, Regex friendIdPattern)
        {
            string dataHovercard = link.GetAttribute("data-hovercard");
            if (dataHovercard.Contains(".php"))
            {
                Match m = friendIdPattern.Match(dataHovercard);
                if (m.Success)
                    userIds.Add(m.Value);

            }
            if (link.GetAttribute("href").Contains("show_more.php")
                || (link.InnerText != null && link.InnerText.Contains("Pokaż inne"))
                || link.GetAttribute("className").Contains("showAll"))
            {
                link.InvokeMember("Click");
            }
        }
    }
}
