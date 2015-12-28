using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Facegraph_Savage
{
    class FacebookRelatedStrings
    {
        private static FacebookRelatedStrings _instance = null;

        private FacebookRelatedStrings() { }

        public static FacebookRelatedStrings getInstance()
        {
            if (_instance == null)
                _instance = new FacebookRelatedStrings();
            return _instance;
        }

        private readonly string url = "http://facebook.com";
        private readonly string urlWithProfilePrefix = "https://www.facebook.com/profile.php?id=";
        private readonly string favoritesSuffix = "&sk=favorites";
        private readonly string friendsSuffix = "&sk=friends";
        private readonly string graphApiUrl = "http://graph.facebook.com/";

        public string GraphApiUrl
        {
            get { return graphApiUrl; }
        } 

        public string FriendsSuffix
        {
            get { return friendsSuffix; }
        } 

        public string FavoritesSuffix
        {
            get { return favoritesSuffix; }
        } 

        public string UrlWithProfilePrefix
        {
            get { return urlWithProfilePrefix; }
        } 

        public string Url
        {
            get { return url; }
        } 


    }
}
