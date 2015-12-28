using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.Runtime.Serialization.Json;
using System.IO;

namespace Facegraph_Savage
{
    abstract class SourceContentSaver
    {
        
        private WebBrowser _webBrowser = new WebBrowser();
        private string _url = null;
        private IContentManager _contentManager = null;
        private string _fileName = null;
        protected CommonResources common = CommonResources.getInstance();
        protected FacebookRelatedStrings facebook = FacebookRelatedStrings.getInstance();

        protected WebBrowser webBrowser
        {
            get { return _webBrowser; }
            set { _webBrowser = value; }
        }

        protected string Url
        {
            get { return _url; }
            set { _url = value; }
        }

        protected string FileName
        {
            get { return _fileName; }
            set { _fileName = value; }
        }

        protected IContentManager contentManager
        {
            get { return _contentManager; }
            set { _contentManager = value; }
        }

        private void prepareWebBrowser()
        {
            webBrowser = new WebBrowser();
            webBrowser.ScriptErrorsSuppressed = true;
            webBrowser.Width = 1000;
            int maxHeight = 65535;
            webBrowser.Height = maxHeight;
            webBrowser.Navigate(Url);
        }

        private Layouts defineLayout()
        {
            while ((webBrowser.Document == null) || (webBrowser.Document.Body == null))     // Zabezpieczyc przed zapetleniem
                Application.DoEvents();

            HtmlDocument document = webBrowser.Document;
            HtmlElement body = document.Body;
            if (body.GetAttribute("className").Contains("timelineLayout"))
                return Layouts.Timeline;
            return Layouts.NoTimeline;
        }

        protected abstract void createContentManager(Layouts layout);

        private void saveToFile(ref ISet<string> ids)
        {
            ISet<Element> elementSet= new HashSet<Element>();
            foreach (string id in ids)
            {
                Element element = new Element();
                element.id = id;
                elementSet.Add(element);
            }
            Element[] elementArray = elementSet.ToArray();
            ElementList elementList = new ElementList();
            elementList.data = elementArray;

            DataContractJsonSerializer serializer = new DataContractJsonSerializer(elementList.GetType());

            Directory.CreateDirectory(Path.GetDirectoryName(FileName));
            Stream stream = new FileStream(FileName, FileMode.Create);
            serializer.WriteObject(stream, elementList);
            stream.Close();
        }


        public ISet<string> saveFileAndGetIds()
        {
            prepareWebBrowser();
            createContentManager(defineLayout());
            contentManager.loadDynamicContent();
            ISet<string> ids = contentManager.getContent();
            webBrowser.Dispose();
            saveToFile(ref ids);
            return ids;
        }
    }
}
