using System;
using System.IO;
using System.Net;
using System.Text;

namespace ApplicationPC
{
    public enum httpVerb
    {
        GET,
        POST,
        PUT
    }
    public enum AutResponse
    {
        Suceeded,
        Failed
    }
    public class RestClient
    {
        public string uri { get; set; }
        public string endPoint { get; set; }
        public httpVerb httpMethod { get; set; }
        public string credentials { get; set; }

        public RestClient()
        {
            uri = string.Empty;
            endPoint = string.Empty;
            httpMethod = httpVerb.POST;
            credentials = string.Empty;
        }


        public AutResponse Authenticate()
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri + "/" + endPoint);

                request.Method = httpMethod.ToString();

                request.Headers.Add("Authorization", "Basic " + credentials);

                using (HttpWebResponse response = (HttpWebResponse)request.GetResponse()) 
                { 
                    if (response.StatusCode == HttpStatusCode.OK) {
                        return AutResponse.Suceeded;
                    } else {
                        return AutResponse.Failed;
                    }
                } 
            } catch (Exception)
            {
                return AutResponse.Failed;
            }
        }

        public string AuthenticateStr()
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri + "/" + endPoint);

                request.Method = httpMethod.ToString();

                request.Headers.Add("Authorization", "Basic " + credentials);

                using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
                {
                    return response.StatusCode.ToString();
                }
            }
            catch (Exception ex)
            {
                return ex.Message.ToString();
            }
        }

        public string MakeRequest()
        {
            try
            {
                string responseValue = string.Empty;

                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri + "/" + endPoint);

                request.Method = httpMethod.ToString();

                request.Headers.Add("Authorization", "Basic " + credentials);

                using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
                {
                    if (response.StatusCode != HttpStatusCode.OK)
                    {
                        throw new ApplicationException("error code " + response.StatusCode);
                    }
                    // Process response stream
                    using (Stream responseStream = response.GetResponseStream())
                    {
                        if (responseStream != null)
                        {
                            using (StreamReader reader = new StreamReader(responseStream))
                            {
                                responseValue = reader.ReadToEnd();
                            }
                        }
                    }
                }
                return responseValue;
            } catch (Exception e)
            {
                return e.ToString();
            }
            
        }
    }
}
