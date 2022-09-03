/**
 * Copyright (c) 2018 Araf Karsh Hamid

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.

 * This program and the accompanying materials are licensed based on Apache 2 License.
 */
package io.fusion.air.microservice.domain.entities.core;

import org.slf4j.MDC;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Record Audit Log - Keeps track of the following fields for all the records. 
 *
 * 1. Created Time Stamp
 * 2. Created By User
 * 3. Updated Time Stamp
 * 4. Updated By User
 *
 * @author Araf Karsh Hamid
 * @version 1.0
 * @date
 */

@Embeddable
public class AuditLog  {

    private static final long serialVersionUID = 1L;

    /**
     * By Setting it to updatable false, 
     * Record Audit log will always have the original data.
     * Record Created By User 
     */
    @Column(name = "createdBy", updatable=false)
    private String createdBy;

    /**
     * By Setting it to updatable false,
     * Record Audit log will always have the original data.
     * Record Created Time
     */
    @Column(name = "createdTime", updatable=false)
    private java.sql.Timestamp createdTime;

    /**
     * Set the updated by user.
     */
    @Column(name = "updatedBy")
    private String updatedBy;

    /**
     * Set the Updated By Time by the user.
     */
    @Column(name = "updatedTime")
    private java.sql.Timestamp updatedTime;

    /**
     * Default System Audit Log
     */
    public AuditLog() {
        createdTime 	= new Timestamp(new Date().getTime());
        createdBy       = MDC.get("user") == null ? "admin" : MDC.get("user");
        updatedBy       = createdBy;
        updatedTime		= new Timestamp(new Date().getTime());
    }

    /**
     * Record Audit Log created by the logged in User.
     *
     * @param _userId
     */
    public AuditLog(String _userId) {
        createdBy		= _userId;
        createdTime 	= new Timestamp(new Date().getTime());
        updatedBy		= _userId;
        updatedTime		= new Timestamp(new Date().getTime());
    }

    /**
     * Set Updated By user for  the record with the current time.
     *
     * @param _userId
     */
    public void setUpdatedBy(String _userId) {
        updatedTime	= new Timestamp(new Date().getTime());
        updatedBy		= _userId;
    }

    /**
     * Get the Record Created Time Stamp
     *
     * @return Timestamp
     */
    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    /**
     * Get the user who created this record.
     *
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Get the Record Updated Time
     *
     * @return Timestamp
     */
    public java.sql.Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**
     * Get the Record updated User ID
     *
     * @return String (UserId)
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Returns the Record Audit Log in a String with the following fields
     *
     * 1. Created Time
     * 2. Created By
     * 3. Updated Time
     * 4. Updated By
     *
     * @return String
     */
    public String toString() {
        return new StringBuilder()
                .append(createdTime).append("|")
                .append(createdBy).append("|")
                .append(updatedTime).append("|")
                .append(updatedBy)
                .toString();
    }
}