package io.humanplz.humanplz;


import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class URLDataDownload extends AsyncTask<String, String, String> {

    private static final Logger log = Logger.getLogger( URLDataDownload.class.getName() );

    private static ArrayList<DataEntry> data = new ArrayList<DataEntry>();

    private static boolean finishedDownloading = false;

    public static boolean isFinishedDownloading() {
        return finishedDownloading;
    }


    public static ArrayList<DataEntry> getData() {
        return data;
    }

    public void setData(ArrayList<DataEntry> data) {
        this.data = data;
    }

     public URLDataDownload() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            log.info("doInBackground asdf");
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {

                    if(lineNumber > 0) { // first line is just description
                        // Deal with the line
                        log.info("apa" + line);
                        StringTokenizer strTOk = new StringTokenizer(line, ";");
                        int columnNumber = 0;
                        DataEntry dataEntry = new DataEntry();
                        while(strTOk.hasMoreElements()) {
                            String nextToken = strTOk.nextToken();
                            if(columnNumber == 0) {
                                dataEntry.setName(nextToken);
                            }
                            else if(columnNumber == 1) {
                                dataEntry.setPhoneNumber(nextToken);
                            }
                            else if(columnNumber == 2) {
                                dataEntry.setDtfmCode(nextToken);
                            }
                            else if(columnNumber == 3) {
                                dataEntry.setCountry(nextToken);
                            }
                            columnNumber++;
                        }
                        data.add(dataEntry);

                    }

                    lineNumber++;
                }
                finishedDownloading = true;
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {

        }


        @Override
        protected void onPostExecute(String file_url) {

        }
    }