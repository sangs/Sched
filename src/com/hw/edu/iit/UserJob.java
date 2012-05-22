/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hw.edu.iit;

/**
 *
 * @author Sangeetha R
 */
public class UserJob implements Comparable {

  private int computeTime = 0;  //Worstcase computation time of the job as per requirement
  private int rdeadline = 0;    //Relative deadline of the job as per requirement
  private String jName = null;  //Job Name
  private int arrivalTime = -1; //Job arrival time

  private int schedStartTime = 0;       //Start time of the job upon scheduling
  private int schedFinishTime = 0;      //Finish time of the job upon scheduling
  private boolean isrdlMet = false;     //Indicates if Relative deadline was met by scheduling

    public UserJob() {

    }

    public UserJob(String name, int arrTime, int cmpTime, int rdl) {
        jName = name;
        arrivalTime = arrTime;
        computeTime = cmpTime;
        rdeadline = rdl;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getComputeTime() {
        return computeTime;
    }

    public void setComputeTime(int computeTime) {
        this.computeTime = computeTime;
    }

    public String getjName() {
        return jName;
    }

    public void setjName(String jName) {
        this.jName = jName;
    }

    public int getRdeadline() {
        return rdeadline;
    }

    public void setRdeadline(int rdeadline) {
        this.rdeadline = rdeadline;
    }

    public boolean isrdlMetByScheduling() {
        return isrdlMet;
    }

    public int getSchedFinishTime() {
        return schedFinishTime;
    }

    public void setSchedFinishTime(int schedFinishTime) {
        this.schedFinishTime = schedFinishTime;
    }

    public int getSchedStartTime() {
        return schedStartTime;
    }

    public void setSchedStartTime(int schedStartTime) {
        this.schedStartTime = schedStartTime;
    }

    public int compareTo (Object obj) {
        int ret = 0;
        UserJob otherJob = (UserJob)obj;

        int rdlThisJob = this.rdeadline;
        int rdlOtherJob = otherJob.rdeadline;
        ret = (rdlThisJob > rdlOtherJob) ? 1 : -1;

        //NOTE: ret == 0 is same deadline, r == 1 is thisJob has later deadline and
        //r == -1 is thisJob has earliest deadline
        return ret;
    }


}   //UserJob
