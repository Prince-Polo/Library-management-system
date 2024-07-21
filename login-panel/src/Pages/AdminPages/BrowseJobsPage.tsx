import React, { useState, useCallback, useEffect } from 'react';
import AdminLayout from './AdminLayout';
import { ApprovedJobMessage } from 'Plugins/JobAPI/ApprovedJobMessage';
import { DeleteJobMessage } from 'Plugins/JobAPI/DeleteJobMessage';
import { JobStudentsQueryMessage } from 'Plugins/JobAPI/JobStudentsQueryMessage';
import { UpdateTaskStatusMessage } from 'Plugins/JobAPI/UpdateTaskStatusMessage';
import { UpdateVolunteerHourMessage } from 'Plugins/StudentAPI/UpdateVolunteerHourMessage';
import { CheckStudentTaskStatusMessage } from 'Plugins/JobAPI/CheckStudentTaskStatusMessage';
import { VolunteerStatusFalseMessage } from 'Plugins/StudentAPI/VolunteerStatusFalseMessage';
import { VolunteerStatusTrueMessage } from 'Plugins/StudentAPI/VolunteerStatusTrueMessage';
import { ForceEndTaskMessage } from 'Plugins/JobAPI/ForceEndTaskMessage'

import { sendPostRequest } from 'Pages/SharedComponents/ErrorMessage';
import { Job } from 'Plugins/GlobalVariables/JobSetting';

interface UpdateVolunteerStatusMessage {
    type: string;
    number: string;
}

interface CheckStudentTaskStatusResponse {
    hasActiveTasks: boolean;
}

interface Student {
    studentId: string;
    status: number;
}

const BrowseJobsPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [approvedJobs, setApprovedJobs] = useState<Job[]>([]);
    const [selectedJobIds, setSelectedJobIds] = useState<Set<number>>(new Set<number>());
    const [jobToDelete, setJobToDelete] = useState<number | null>(null);
    const [students, setStudents] = useState<Map<number, Student[]>>(new Map());
    const [selectedStudent, setSelectedStudent] = useState<Student | null>(null);
    const [selectedJobForStudent, setSelectedJobForStudent] = useState<number | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [isPassFailModal, setIsPassFailModal] = useState(false);

    const fetchApprovedJobs = useCallback( () => {
        setError(null);

        const message= new ApprovedJobMessage();
        try {
           sendPostRequest(message,setError,(response)=>{
               try {
                   const parsedResponse = JSON.parse(response);
                   if (!Array.isArray(parsedResponse)) {
                       setError('Invalid data format received: expected an array');
                   }
                   const jobs = parsedResponse.map((job: any) => ({
                       jobId: job.jobId,
                       jobShortDescription: job.jobShortDescription,
                       jobLongDescription: job.jobLongDescription,
                       jobHardness: job.jobHardness,
                       jobCredit: job.jobCredit,
                       jobCurrent: job.jobCurrent,
                       jobEnrolled: job.jobEnrolled,
                       jobRequired: job.jobRequired
                   }));
                   // Automatically delete jobs with jobEnrolled and jobRequired both being 0
                   for (const job of jobs) {
                       if (job.jobEnrolled === 0 && job.jobRequired === 0) {
                           autoDeleteJob(job.jobId);
                       }
                   }
                   setApprovedJobs(jobs.filter((job:Job)=> !(job.jobEnrolled === 0 && job.jobRequired === 0)));
               } catch (e) {
                   console.error('Failed to parse response JSON:', e);
                   setError('Failed to parse response JSON');
               }
           })
        } catch (error) {
            setError('Failed to fetch jobs');
            console.error('Error fetching jobs:', error);
        }
    }, []);

    const autoDeleteJob = (jobId: number) => {
        const message=new DeleteJobMessage(jobId);
        try {
            sendPostRequest(message,setError);
        } catch (error) {
            console.error('Error auto-deleting job:', error);
        }
    };

    const fetchJobStudents = useCallback(async (jobId: number) => {
        const message=new JobStudentsQueryMessage(jobId);
        try {
           sendPostRequest(message,setError,(response)=>{
               try {
                    const parsedResponse = JSON.parse(response);
                   if (!Array.isArray(parsedResponse)) {
                       throw new Error('Invalid data format received: expected an array');
                   }
                   const jobStudents = parsedResponse.map((student: any) => ({
                       studentId: student.studentId,
                       status: student.status
                   }));
                   setStudents(prev => new Map(prev).set(jobId, jobStudents.filter(s => s.status !== 2)));
                } catch (e) {
                   console.error('Failed to parse response JSON:', e);
                   setError('Failed to parse response JSON');
                }
              })
        } catch (error) {
            setError('Failed to fetch job students');
            console.error('Error fetching job students:', error);
        }
    }, []);

    useEffect(() => {
        fetchApprovedJobs();
    }, [fetchApprovedJobs]);

    const handleRowClick = (jobId: number) => {
        setSelectedJobIds(prevSelectedJobIds => {
            const newSelectedJobIds = new Set(prevSelectedJobIds);
            if (newSelectedJobIds.has(jobId)) {
                newSelectedJobIds.delete(jobId);
            } else {
                newSelectedJobIds.add(jobId);
                fetchJobStudents(jobId);  // Fetch students when a job row is clicked
            }
            return newSelectedJobIds;
        });
    };

    const handleDeleteJob = (jobId: number) => {
        setJobToDelete(jobId);
    };

    const confirmDeleteJob = () => {
        if (jobToDelete === null) return;

        setError(null);

        const message=new DeleteJobMessage(jobToDelete);

        try {
           sendPostRequest(message,setError,(response)=>{
               setJobToDelete(null);
                fetchApprovedJobs();

           })
        } catch (error) {
            setError('Failed to delete job');
            console.error('Error deleting job:', error);
        }
    };

    const handleUpdateStudentStatus = (student: Student, jobId: number, isPassFail: boolean = false) => {
        setSelectedStudent(student);
        setSelectedJobForStudent(jobId);
        setIsPassFailModal(isPassFail);
        setShowModal(true);
    };

    const confirmUpdateStudentStatus = async (status: number) => {
        if (selectedStudent === null || selectedJobForStudent === null) return;

        const message=new UpdateTaskStatusMessage (selectedJobForStudent, selectedStudent.studentId, status);

        try {
            sendPostRequest(message,setError);
            // 如果状态从3变为4，更新志愿者小时数
            if (selectedStudent.status === 3 && status === 4) {
                const updateVolunteerHourMessage=new UpdateVolunteerHourMessage(selectedJobForStudent, selectedStudent.studentId);
               sendPostRequest(updateVolunteerHourMessage,setError);
            }

            // 检查并更新 volunteer_status
            if ((selectedStudent.status === 0 && status === 1) || (selectedStudent.status === 1 && status === 5)) {
                // 检查是否还有状态为 1 的任务
                const checkMessage=new CheckStudentTaskStatusMessage(selectedStudent.studentId);

                sendPostRequest(checkMessage,setError,(checkResponse)=>{
                    if (checkResponse.ok) {
                    const checkResult=JSON.parse(checkResponse);
                    // 根据检查结果更新学生的 volunteer_status
                    if(checkResult){
                        const volunteerStatusTrueMessage=new VolunteerStatusTrueMessage(selectedStudent.studentId);
                        sendPostRequest(volunteerStatusTrueMessage,setError);
                    }
                    else{
                        const volunteerStatusFalseMessage=new VolunteerStatusFalseMessage(selectedStudent.studentId);
                        sendPostRequest(volunteerStatusFalseMessage,setError);
                    }
                }})
            }
            fetchJobStudents(selectedJobForStudent);
            fetchApprovedJobs(); // Fetch jobs to update the counts
            setShowModal(false);

        } catch (error) {
            setError('Failed to update student status');
            console.error('Error updating student status:', error);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedStudent(null);
        setSelectedJobForStudent(null);
        setIsPassFailModal(false);
    };

    const handleForceEndTask = async (studentId: string, jobId: number) => {
        const message = new ForceEndTaskMessage(jobId, studentId);
        try {
           sendPostRequest(message,setError);
            // 检查是否还有状态为1的任务
            const checkMessage=new CheckStudentTaskStatusMessage(studentId);

           sendPostRequest(checkMessage,setError,(checkResponse)=> {
                   // 根据检查结果更新学生的volunteer_status
               const checkResult=JSON.parse(checkResponse);
               console.log(checkResult);
               if(checkResult){
                   const volunteerStatusTrueMessage=new VolunteerStatusTrueMessage(studentId);
                   sendPostRequest(volunteerStatusTrueMessage,setError);
               }
                else{
                     const volunteerStatusFalseMessage=new VolunteerStatusFalseMessage(studentId);
                     sendPostRequest(volunteerStatusFalseMessage,setError);
                }
           });
            fetchJobStudents(jobId);
            fetchApprovedJobs(); // Fetch jobs to update the counts
        } catch (error) {
            setError('Failed to force end task');
            console.error('Error force ending task:', error);
        }
    };

    return (
        <AdminLayout>
            <h2 style={headingStyle}>Browse Available Jobs</h2>
            {error && <p style={errorMessageStyle}>{error}</p>}
            <table style={jobsTableStyle}>
                <thead>
                <tr>
                    <th style={thStyle}>Short Description</th>
                    <th style={thStyle}>Hardness</th>
                    <th style={thStyle}>Credit</th>
                    <th style={thStyle}>Current / Enrolled / Required</th>
                    <th style={thStyle}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {approvedJobs.map((job: Job) => (
                    <React.Fragment key={job.jobId}>
                        <tr onClick={() => handleRowClick(job.jobId)} style={trStyle}>
                            <td style={tdStyle}>{job.jobShortDescription}</td>
                            <td style={tdStyle}>{job.jobHardness}</td>
                            <td style={tdStyle}>{job.jobCredit}</td>
                            <td style={tdStyle}>{job.jobCurrent} / {job.jobEnrolled} / {job.jobRequired}</td>
                            <td style={tdStyle}>
                                <button onClick={() => handleDeleteJob(job.jobId)} style={deleteButtonStyle} disabled={job.jobEnrolled > 0}>Delete</button>
                            </td>
                        </tr>
                        {selectedJobIds.has(job.jobId) && (
                            <>
                                <tr style={trStyle}>
                                    <td colSpan={5} style={tdStyle}>
                                        <strong>Long Description:</strong> {job.jobLongDescription}
                                    </td>
                                </tr>
                                <tr style={trStyle}>
                                    <td colSpan={5} style={tdStyle}>
                                        <table style={jobsTableStyle}>
                                            <thead>
                                            <tr>
                                                <th style={thStyle}>Student ID</th>
                                                <th style={thStyle}>Status</th>
                                                <th style={thStyle}>Action</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {students.get(job.jobId)?.map(student => (
                                                <tr key={student.studentId}>
                                                    <td style={tdStyle}>{student.studentId}</td>
                                                    <td style={tdStyle}>
                                                        {student.status === 0 ? 'Pending' : student.status === 1 ? 'Approved' : student.status === 2 ? 'Rejected' : student.status === 3 ? 'Submitted' : student.status === 4 ? 'Completed (Passed)' : 'Completed (Failed)'}
                                                    </td>
                                                    <td style={tdStyle}>
                                                        {student.status === 0 && job.jobRequired !== 0 && (
                                                            <button
                                                                style={studentButtonStyle}
                                                                onClick={() => handleUpdateStudentStatus(student, job.jobId)}
                                                            >
                                                                Change Status
                                                            </button>
                                                        )}
                                                        {student.status === 3 && (
                                                            <button
                                                                style={studentButtonStyle}
                                                                onClick={() => handleUpdateStudentStatus(student, job.jobId, true)}
                                                            >
                                                                Approve or Reject Submission
                                                            </button>
                                                        )}
                                                        {student.status !== 0 && student.status !== 3 && student.status !== 4 && student.status !== 5 && (
                                                            <button
                                                                style={studentButtonStyle}
                                                                onClick={() => handleForceEndTask(student.studentId, job.jobId)}
                                                            >
                                                                Force End Task
                                                            </button>
                                                        )}
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </>
                        )}
                    </React.Fragment>
                ))}
                </tbody>
            </table>
            {jobToDelete !== null && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Are you sure you want to delete this job?</h3>
                        <button onClick={confirmDeleteJob} style={confirmButtonStyle}>Yes</button>
                        <button onClick={() => setJobToDelete(null)} style={cancelButtonStyle}>No</button>
                    </div>
                </div>
            )}
            {showModal && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Change Student Status</h3>
                        {isPassFailModal ? (
                            <>
                                <button onClick={() => confirmUpdateStudentStatus(4)} style={confirmButtonStyle}>Pass</button>
                                <button onClick={() => confirmUpdateStudentStatus(5)} style={cancelButtonStyle}>Fail</button>
                            </>
                        ) : (
                            <>
                                <button onClick={() => confirmUpdateStudentStatus(1)} style={confirmButtonStyle}>Approve</button>
                                <button onClick={() => confirmUpdateStudentStatus(2)} style={cancelButtonStyle}>Reject</button>
                            </>
                        )}
                        <button onClick={handleCloseModal} style={cancelButtonStyle}>Cancel</button>
                    </div>
                </div>
            )}
        </AdminLayout>
    );
};

const headingStyle: React.CSSProperties = {
    marginBottom: '20px',
    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif',
    color: '#333',
};

const jobsTableStyle: React.CSSProperties = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px',
};

const thStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
    background: '#f0f8ff',
    fontWeight: 'bold',
};

const trStyle: React.CSSProperties = {
    cursor: 'pointer',
};

const tdStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
};

const deleteButtonStyle: React.CSSProperties = {
    background: '#ff6347',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'pointer',
};

const errorMessageStyle: React.CSSProperties = {
    color: 'red',
    fontWeight: 'bold',
};

const modalStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    background: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const modalContentStyle: React.CSSProperties = {
    background: 'white',
    padding: '20px',
    borderRadius: '10px',
    textAlign: 'center',
};

const confirmButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

const cancelButtonStyle: React.CSSProperties = {
    background: '#ff6347',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

const studentButtonStyle: React.CSSProperties = {
    marginLeft: '10px',
    background: '#007bff',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'pointer',
};

export default BrowseJobsPage;
