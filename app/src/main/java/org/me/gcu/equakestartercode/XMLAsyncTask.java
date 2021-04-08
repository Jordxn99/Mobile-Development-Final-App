package org.me.gcu.equakestartercode;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class XMLAsyncTask extends AsyncTask <String, Void, String> {

    private ReturnResult returnResult;
    private String result;
    public XMLAsyncTask(ReturnResult returnResult) {
        this.returnResult = returnResult;
    }

    @Override
    protected String doInBackground(String... strings) {

        String url = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";


        Log.e("MyTag","in run");

        try
        {
            Log.e("MyTag","in try");
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            Log.e("MyTag","after ready");
            //
            // Throw away the first 2 header lines before parsing
            //
            //
            //
            while ((inputLine = in.readLine()) != null)
            {
                result = result + inputLine;
                Log.e("MyTag",inputLine);

            }
            in.close();
        }
        catch (IOException ae)
        {
            Log.e("MyTag", "ioexception in run");
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d("UI thread", "I am the UI thread");
        String tempoo = s.replace("null", "");

        LinkedList<Quake> quakeLinkedList;

        quakeLinkedList = parseData(tempoo);
        String tempo = "";
        Collections.sort(quakeLinkedList,Collections.reverseOrder());

        returnResult.returnQuake(quakeLinkedList);

    }

    private LinkedList<Quake> parseData(String dataToParse)
    {
        Quake quake = new Quake();
        LinkedList <Quake> alist = new LinkedList<Quake>();
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        quake = new Quake();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        quake.setTitle(temp);
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            quake.setPubDate(temp);
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("description"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                quake.setDescription(temp);
                            }
                            else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("lat"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    quake.setGeolat(temp);
                                }
                                else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("long"))
                                    {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        // Do something with text
                                        quake.setGeolong(temp);
                                    }
                    if (xpp.getName().equalsIgnoreCase("link"))
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        quake.setLink(temp);
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("category"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            quake.setCategory(temp);
                        }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        alist.add(quake);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return alist;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return alist;

    }



}
