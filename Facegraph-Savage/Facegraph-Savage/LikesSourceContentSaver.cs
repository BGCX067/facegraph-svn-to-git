using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    class LikesSourceContentSaver : SourceContentSaver
    {
        public LikesSourceContentSaver(string userID)
        {
            Url = facebook.UrlWithProfilePrefix + userID + facebook.FavoritesSuffix;
            FileName = common.Path + "\\" + userID + "\\" + "likes";
        }

        protected override void createContentManager(Layouts layout)
        {
            contentManager = new LikesContentManager(webBrowser);
        }
    }
}
