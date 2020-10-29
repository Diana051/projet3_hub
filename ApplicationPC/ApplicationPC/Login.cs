
using System;

using System.Net.Http;

using System.Text;
using System.Threading.Tasks;

using System.Windows.Forms;


namespace ApplicationPC
{
    public partial class Login : Form
    {
        public RestClient client;

        private string GetURI()
        {
            string IPAddress = txtIPAddress.Text;
            int port = 8000;
            return $"http://{IPAddress}:{port}";
            //return $"http://{IPAddress}";
        }
        public Login()
        {
            InitializeComponent();
            client = new RestClient();
        }
        private string GetCredentials()
        {
            string password = txtPassword.Text;
            return System.Convert.ToBase64String(System.Text.ASCIIEncoding.ASCII.GetBytes("admin:" + password));
        }
        void VerifyPassword()
        {
            client.httpMethod = httpVerb.POST;
            client.uri = GetURI();
            client.endPoint = "server/usager/login";
            client.credentials = GetCredentials();

            AutResponse response = client.Authenticate();
            switch (response)
            {
                case AutResponse.Suceeded:
                    OpenMainForm();
                    break;
                case AutResponse.Failed:
                    MessageBox.Show(client.AuthenticateStr());
                    break;
            }
        }
        public void OpenMainForm()
        {
            Main mainForm = new Main(client);
            this.Hide();
            mainForm.ShowDialog();
        }
        #region UI Event Handlers
        private void button1_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtIPAddress.Text)) {
                MessageBox.Show("L'adresse IP ne peut etre vide", "Erreur", MessageBoxButtons.OK);
            } else if (string.IsNullOrEmpty(txtPassword.Text)) {
                MessageBox.Show("Le mot de passe ne peut etre vide", "Erreur", MessageBoxButtons.OK);
            }
            VerifyPassword();
        }
        private void Login_Load(object sender, EventArgs e)
        {

        }
        #endregion


    }
}
