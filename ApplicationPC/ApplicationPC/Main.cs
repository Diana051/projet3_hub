using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Newtonsoft.Json;

namespace ApplicationPC
{
    public partial class Main : Form
    {
        private RestClient client { get; set; }
        
        public Main (RestClient cl)
        {
            InitializeComponent();
            client = cl;
            fetchSurvey();
        }

        private void fetchSurvey()
        {
            client.endPoint = "server/sondage";
            client.httpMethod = httpVerb.GET;
            string survey = client.MakeRequest();
            AfficherSondage(survey);
        }
        #region json funct
        private void AfficherSondage(string json)
        {
            if(!string.IsNullOrEmpty(json)) {
                try
                {
                    var jPerson = JsonConvert.DeserializeObject<List<jsonPerson>>(json);

                    string txt = "Voici les résultats du sondage :" + Environment.NewLine;

                    foreach (var person in jPerson)
                    {
                        if (person.courriel != null) {
                            txt += "Courriel : " + person.courriel + Environment.NewLine;
                            txt += "Prénom : " + person.prenom + Environment.NewLine;
                            txt += "Nom : " + person.nom + Environment.NewLine;
                            txt += "Âge : " + person.age + Environment.NewLine;
                            txt += "Intérêt : " + person.interet + Environment.NewLine + Environment.NewLine;
                        }
                    }
                    txtSondage.Text = txt;
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message.ToString());
                }
            } else {
                txtSondage.Text = "Le sondage est vide";
            }
            
        }
        #endregion
        #region UI Event Handlers
        private void Main_Load(object sender, EventArgs e) {}

        private void button1_Click(object sender, EventArgs e)
        {
            fetchSurvey();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }
#endregion
    }
}
