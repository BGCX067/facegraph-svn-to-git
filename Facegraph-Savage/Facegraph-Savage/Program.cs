using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Collections;

namespace Facegraph_Savage
{
    static class Program
    {
        private static List<string> argList;
        private const string wrongCommandLine = "Very bad exception! Application stopped!. (Wrong command line arguments)";
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Login login = new Login();
            if (args.Length > 0)
            {
                if (args.Length % 2 == 0)
                {
                    argList = args.ToList();
                    while (argList.Count != 0)
                    {
                        switch (argList[0])
                        {
                            case "-p":
                                argList.RemoveAt(0);
                                login.setPath(argList[0]);
                                argList.RemoveAt(0);
                                break;
                            case "-d":
                                argList.RemoveAt(0);
                                login.setDepth(argList[0]);
                                argList.RemoveAt(0);
                                break;
                            case "-i":
                                argList.RemoveAt(0);
                                login.setStartingProfile(argList[0]);
                                argList.RemoveAt(0);
                                break;
                            default:
                                argList.Clear();
                                MessageBox.Show(wrongCommandLine);
                                break;
                        }
                    }
                }
                else
                {
                    MessageBox.Show(wrongCommandLine);
                }


            }
            Application.Run(login);

        }
    }
}
