/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hw.edu.iit;

import java.util.*;
import java.io.*;
import static com.hw.edu.iit.MySchedulerConstants.*;

/**
 *
 * @author Sangeetha R
 */
public class ScheduleUserJob {

    private static PriorityQueue<UserJob> userJobPQ;
    private static ArrayList<JobSchedule> jobSchedList;
    //Check for schedulability of the list of jobs based on their deadline
    private double utilizationRatio = 0;
    
    public ScheduleUserJob() {
        jobSchedList = new ArrayList<JobSchedule> ();
        userJobPQ = new PriorityQueue<UserJob>();
    }
    
    public void doSchedule (File inputJList) {
       String fName = inputJList.getAbsolutePath();
       String line = null;
       UserJob userJobObj = null;
       boolean status = false;
       utilizationRatio = 0;
       double ratio = 0;

       try {
            BufferedReader in = new BufferedReader ( new InputStreamReader
							( new FileInputStream ( inputJList ) ) );
            while ( (line = in.readLine()) != null ) {
              //Ignore comments
              if ( line.charAt(0) != COMMENTS_LINE ) {
                //Read each line from the input file, create the UserJob and add to the UserJob PQ
                userJobObj = process_user_job (line);
                status = userJobPQ.offer(userJobObj);
                ratio = (double)( (double)userJobObj.getComputeTime() / (double)userJobObj.getRdeadline() );
                utilizationRatio += ratio;
              }
            }
       }
       catch (IOException io) {
            System.out.println ("Error reading input file with list of jobs to be scheduled. File is " + fName);
            io.printStackTrace();
       }
       catch (Exception ex) {
            ex.getMessage();
       }

       //EDF Scheduling
       doEDFScheduling ();
    }


    UserJob process_user_job (String line) {
        UserJob uJob = null;
        String[] jbcs = new String[4]; //Job Characteristics
        String jName;
        int aTime, cTime, dTime;
       
        jbcs = line.split(JLIST_SEPERATOR, 4);
        jName = jbcs[0];
        aTime = Integer.parseInt(jbcs[1]);
        cTime = Integer.parseInt(jbcs[2]);
        dTime = Integer.parseInt(jbcs[3]);
        uJob = new UserJob (jName, aTime, cTime, dTime);
        
        return uJob;
    }

    void doEDFScheduling () {
        //userJobPQ is the input and output is stored in jobSchedList
        int startTm = 0;
        int finishTm = 0;
        int cmpTime = 0;
        int rdlTime = 0;
        String jName = null;
        UserJob userJobObj = null;
        JobSchedule jSchedule = null;

        while(userJobPQ.size() > 0) {
          //Here jobs are taken from PQ as per the EDF priority set
          //for the UserJob object. Refer to CompareTo in UserJob class
          userJobObj = userJobPQ.remove();
          jName = userJobObj.getjName();
          startTm = finishTm;
          cmpTime = userJobObj.getComputeTime();
          finishTm += cmpTime;
          rdlTime = userJobObj.getRdeadline();

          //JobSchedules as per EDF policy are now stored in ArrayList of JobSchedule
          jSchedule = new JobSchedule(jName, startTm, finishTm);
          jSchedule.setRdlTimeRequested(rdlTime);
          jobSchedList.add(jSchedule);
        }
    }

    void printEDFJobSchedule(PrintStream outp) {
        //jobSchedList is the input
        JobSchedule js = null;
        String name = null;
        int st = 0;
        int ft = 0;
        int dt = 0;
        String str = null;
        String schedulability = (utilizationRatio < 1) ? WILL_MEET_DEADLINE_EDF : WILL_MISS_DEADLINE_EDF;

        System.out.println("Scheduling policy selected is: " + SCHEDULING_POLICY);
        System.out.println("Scheduling type is           : " + SCHEDULING_TYPE);
        System.out.println("Schedulability check: " + schedulability);
        System.out.println("Printing jobs in the order they are scheduled. Schedules are in the format: [start time, finish time]");

        if (outp != null) {
          outp.println("Scheduling policy selected is: " + SCHEDULING_POLICY);
          outp.println("Scheduling type is           : " + SCHEDULING_TYPE);
          outp.println("Schedulability check: " + schedulability);
          outp.println("Printing jobs in the order they are scheduled. Schedules are in the format: [start time, finish time]");
        }

        for (Object obj : jobSchedList) {
            if (obj != null) {
              js = ((JobSchedule)obj);
              name = js.getjName();
              st = js.getsTime();
              ft = js.getfTime();
              dt = js.getRdlTimeRequested();

              str = (ft <= dt) ? "" : "Deadline Exceeded";

              System.out.println (name + ": [" + st + ", " + ft + "]" + ", Deadline = " + dt + " " + str);
              if (outp != null) {
                  outp.println(name + ": [" + st + ", " + ft + "]" + ", Deadline = " + dt + " " + str);
                  outp.println();
              }
            }
        } //for
    } //printEDFJobSchedule


    //////////  Private JobSchedule class ///////////////////////
    private class JobSchedule {
        String jName;
        int sTime;
        int fTime;
        int rdlTimeRequested;
       
        public JobSchedule() {
            jName = null;
            sTime = 0;
            fTime = 0;
            rdlTimeRequested = 0;
        }

        public JobSchedule(String jName, int sTime, int fTime) {
            this.jName = jName;
            this.sTime = sTime;
            this.fTime = fTime;
            this.rdlTimeRequested = 0;
        }

        public int getRdlTimeRequested() {
            return rdlTimeRequested;
        }

        public void setRdlTimeRequested(int rdlTimeRequested) {
            this.rdlTimeRequested = rdlTimeRequested;
        }

        public String getjName() {
            return jName;
        }

        public void setjName(String jName) {
            this.jName = jName;
        }

        public int getfTime() {
            return fTime;
        }

        public void setfTime(int fTime) {
            this.fTime = fTime;
        }

        public int getsTime() {
            return sTime;
        }

        public void setsTime(int sTime) {
            this.sTime = sTime;
        }

              
    } //class JobSchedule


 


} //class ScheduleUserJob
