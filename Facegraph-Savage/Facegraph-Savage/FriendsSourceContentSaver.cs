using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    class FriendsSourceContentSaver : SourceContentSaver
    {
        public FriendsSourceContentSaver(string userID)
        {
            Url = facebook.UrlWithProfilePrefix + userID + facebook.FriendsSuffix;
            FileName = common.Path + "\\" + userID + "\\" + "friends";
        }

        protected override void createContentManager(Layouts layout)
        {
            contentManager = new FriendsContentManager(webBrowser, layout);
        }
    }
}
