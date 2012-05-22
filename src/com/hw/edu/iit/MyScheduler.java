/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hw.edu.iit;

import java.io.*;
import java.util.*;
import static com.hw.edu.iit.MySchedulerConstants.*;


/**
 *
 * @author Sangeetha R
 */

class MyJobScheduler {
    File opFile = null;
    File jbsFile = null;
    PrintStream outp = null;
    ScheduleUserJob scheduleJobsObj = null;
    
    public MyJobScheduler () {

        //Step 1: Read setup file filename and location
        //of the list of jobs to be scheduled. Print note on "relative deadline scheduling"
        //being applied. 
        init();

        lookupJobsAndSchedule();
    }

    void init () {
        String line = null;
        String opLogFileName = null;
        String opLogFileLoc = null;
        String jListFileName = null;
        String jListFileLoc = null;

        String fName = CONFIG_FILE_LOCATION.concat(CONFIG_FILE_NAME);
        File cgFile = new File ( fName );

        if ( !(cgFile.exists() && cgFile.isFile()) ) {
            System.out.println ("Missing configuration information. Looking for file: " + fName);
        }
        //JOBLIST_FILE_LOC
        try {
            BufferedReader in = new BufferedReader ( new InputStreamReader
							( new FileInputStream ( cgFile ) ) );
            while ( ( line = in.readLine() ) != null ) {
                String[] lineStr = line.split ( "=", 2 );
		if ( lineStr.length == 2 ) {
                    if ( lineStr[0].compareTo(JOBLIST_FILE_LOC) == 0 ) {
                        if ( ! isNullOrEmpty(lineStr[1]) )
		   		jListFileLoc = (lineStr[1]).trim();
		    }
		    else if ( lineStr[0].compareTo(JOBLIST_FILE) == 0 ) {
                        if ( ! isNullOrEmpty(lineStr[1]) )
                            jListFileName = (lineStr[1]).trim();
                    }
                    else if ( lineStr[0].compareTo(LOG_FILE_LOC) == 0 ) {
                        if ( ! isNullOrEmpty(lineStr[1]) )
                            opLogFileLoc = (lineStr[1]).trim();
                    }
                    else if ( lineStr[0].compareTo(LOG_FILE) == 0 ) {
                        if ( ! isNullOrEmpty(lineStr[1]) )
                            opLogFileName = (lineStr[1]).trim();
                    }

                } //if
            } //while
            in.close ();

            //Get file pointer to the file with list of jobs to be scheduled
            fName = jListFileLoc.concat(jListFileName);
            jbsFile = new File (fName);
            if ( !(jbsFile.exists() && jbsFile.isFile()) ) {
              System.out.println ("Missing the input file with list of jobs to be scheduled. Looking for file: " + fName);
            }

            //Get file pointer to the output file where schedule results will be logged
            fName = opLogFileLoc.concat(opLogFileName);
            opFile = new File (fName);
            outp = new PrintStream ( new FileOutputStream (opFile) );
       	}
	catch (IOException io) {
            System.out.println ("Config file problem");
            io.printStackTrace();
	}
	catch (Exception ex) {
            ex.getMessage();
	}

    } //init


    void lookupJobsAndSchedule () {
        int user_opt = -1;

        //Step 2: Print menu options and get user selection
        user_opt = getUserOption();

        if (user_opt == 1) {
          //Step 3: Process menu options and schedule
          scheduleJobs();

          //Step 4: Print the schedule output to console (and log to a output file and close log file)
          printSchedule();
        }

        proceed_to_exit();
    }

    int getUserOption () {
      int usrInput = -1;
      Scanner scan = new Scanner(System.in);

      //Print menu options here
      System.out.println("1. Schedule Jobs");
      System.out.println("2. Exit");

      usrInput = scan.nextInt();
      return usrInput;
    }

    void scheduleJobs () {
        scheduleJobsObj = new ScheduleUserJob();
        scheduleJobsObj.doSchedule(jbsFile);
    }

    void printSchedule () {
        if (scheduleJobsObj != null)
          scheduleJobsObj.printEDFJobSchedule(outp);
    }

    private static boolean isNullOrEmpty (String str)
    {
        if( str == null || "".equals( str.trim() ) )
            return true;
	return false;
    }

    void proceed_to_exit() {
      if ( outp != null ) {
        outp.flush();
        outp.close();
      }
    }


} //MyJobScheduler


public class MyScheduler {

    private static MyJobScheduler myJSched = null;

    public static void main(String[] args) {
         myJSched = new MyJobScheduler();
    }

} //public class MyScheduler
