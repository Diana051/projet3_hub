using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ApplicationPC
{
    public partial class Form1 : Form
    {
        public Login loginForm = new Login();

        public Form1()
        {
            InitializeComponent();
            this.Hide();
            this.openLoginForm();
        }
        private void Form1_Load(object sender, EventArgs e) {}

        private void openLoginForm()
        {
            loginForm.ShowDialog();
            this.Close();
        }

        
    }
}
