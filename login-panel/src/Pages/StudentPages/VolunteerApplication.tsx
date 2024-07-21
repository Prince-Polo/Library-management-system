import React, { useState, useEffect, useCallback } from 'react';
import { useHistory } from 'react-router-dom';
import StudentLayout from './StudentLayout';
import { useStore } from 'Plugins/GlobalVariables/store';
import { useToMyCenter } from 'Pages/SharedComponents/LibraryDataLinking';
import { Job,Task } from 'Plugins/GlobalVariables/JobSetting';
import { sendPostRequest,ErrorModal } from 'Pages/SharedComponents/ErrorMessage';
import { ApprovedJobMessage } from 'Plugins/JobAPI/ApprovedJobMessage';
import { StudentSpecificAcceptedJobMessage } from 'Plugins/StudentAPI/StudentSpecificAcceptedJobMessage';
import { UpdateJobCurrentMessage } from 'Plugins/JobAPI/UpdateJobCurrentMessage';
import { AcceptJobMessage } from 'Plugins/StudentAPI/AcceptJobMessage';
import { SubmitJobMessage } from 'Plugins/StudentAPI/SubmitJobMessage';
import { VolunteerStatusFalseUsingTokenMessage } from 'Plugins/StudentAPI/VolunteerStatusFalseUsingTokenMessage'

export const VolunteerApplication: React.FC = () => {
    const {toMyCenter,err:clearToMyCenterError}=useToMyCenter()
    const [error, setError] = useState<string | null>(null);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [selectedJobIds, setSelectedJobIds] = useState<Set<number>>(new Set<number>());
    const [selectedJobId, setSelectedJobId] = useState<number | null>(null);
    const token = useStore((state) => state.info.token);
    const history = useHistory();

    const fetchJobs = useCallback(() => {
        setError(null);
        const message= new ApprovedJobMessage();
        sendPostRequest(message, setError, (response) => {
            try {
                const parsedResponse = JSON.parse(response);
                console.log('approve',parsedResponse);
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
                setJobs(jobs);
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }
        })
    }, []);

    const fetchTasks = useCallback(() => {
        setError(null);
        const message = new StudentSpecificAcceptedJobMessage(token);

        sendPostRequest(message, setError, (response) => {
            try {
                const parsedResponse = JSON.parse(response);
                console.log(parsedResponse);
                if (!Array.isArray(parsedResponse.tasks)) {
                    setError('Invalid data format received: expected an array');
                }

                const tasks = parsedResponse.tasks.map((task: any) => ({
                    jobId: task.jobId,
                    status: task.status
                }));
                setTasks(tasks);
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }
        })
    }, [token]);

    useEffect(() => {
        fetchJobs();
        fetchTasks();
    }, [fetchJobs, fetchTasks]);

    const handleRowClick = (jobId: number) => {
        setSelectedJobIds(prevSelectedJobIds => {
            const newSelectedJobIds = new Set(prevSelectedJobIds);
            if (newSelectedJobIds.has(jobId)) {
                newSelectedJobIds.delete(jobId);
            } else {
                newSelectedJobIds.add(jobId);
            }
            return newSelectedJobIds;
        });
    };

    const handleEnrollClick = (jobId: number) => {
        setSelectedJobId(jobId);
    };

    const confirmEnroll = async () => {
        if (selectedJobId === null) return;

        const updateJobMessage = new UpdateJobCurrentMessage(selectedJobId,1)

        const createTaskMessage = new AcceptJobMessage(token,selectedJobId);
        try {
            sendPostRequest(updateJobMessage, setError, (response) => {
               sendPostRequest(createTaskMessage, setError);
               fetchJobs();
               fetchTasks();})
        }catch (error) {
            setError('Failed to enroll in job');
            console.error('Error enrolling in job:', error);
        }
        setSelectedJobId(null);
    };

    const submitTask = async (jobId: number) => {
        const submitTaskMessage = new SubmitJobMessage(jobId,token);

        try {
            sendPostRequest(submitTaskMessage, setError,(response)=>{
                fetchTasks();
                // 检查是否还有状态为1的任务
                const hasActiveTasks = tasks.some(task => task.jobId !== jobId &&task.status === 1);
                console.log("hasActive",hasActiveTasks)
                // 如果没有状态为1的任务，更新志愿者状态为false
                if (!hasActiveTasks) {
                    const updateVolunteerStatusMessage = new VolunteerStatusFalseUsingTokenMessage(token);
                    sendPostRequest(updateVolunteerStatusMessage, setError);
                }
            });
        } catch (error) {
            setError('Failed to submit task');
            console.error('Error submitting task:', error);
        }
    };

    const getStatusButton = (jobId: number) => {
        const task = tasks.find(task => task.jobId === jobId);
        if (!task) {
            return <button onClick={() => handleEnrollClick(jobId)} style={enrollButtonStyle} disabled={failCount >= 3}>Enroll</button>;
        }
        switch (task.status) {
            case 0:
                return <button style={pendingButtonStyle}>Pending</button>;
            case 1:
                return <button onClick={() => submitTask(jobId)} style={submitButtonStyle}>Submit</button>;
            case 2:
                return <button style={rejectedButtonStyle}>Rejected</button>;
            case 3:
                return <button style={submittedButtonStyle}>Submitted</button>;
            case 4:
                return <button style={passButtonStyle}>Pass</button>;
            case 5:
                return <button style={failButtonStyle}>Fail</button>;
            default:
                return <button onClick={() => handleEnrollClick(jobId)} style={enrollButtonStyle} disabled={failCount >= 3}>Enroll</button>;
        }
    };

    // Helper function to get the status for a job
    const getStatusForJob = (jobId: number) => {
        const task = tasks.find(task => task.jobId === jobId);
        return task ? task.status : -1; // Return -1 if there's no status
    };

    // Calculate the number of fail tasks
    const failCount = tasks.filter(task => task.status === 5).length;

    // Sort jobs based on status
    const sortedJobs = jobs.sort((a, b) => {
        const statusA = getStatusForJob(a.jobId);
        const statusB = getStatusForJob(b.jobId);
        return statusA - statusB;
    });

    // Filter jobs where jobRequired is 0 and status is 0
    const filteredJobs = sortedJobs.filter(job => !(job.jobRequired === 0 && (getStatusForJob(job.jobId) === 0 || getStatusForJob(job.jobId) === -1)));

    return (
        <StudentLayout>
            <h2 style={headingStyle}>Available Jobs</h2>
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
                {filteredJobs.map((job: Job) => (
                    <React.Fragment key={job.jobId}>
                        <tr onClick={() => handleRowClick(job.jobId)} style={trStyle}>
                            <td style={tdStyle}>{job.jobShortDescription}</td>
                            <td style={tdStyle}>{job.jobHardness}</td>
                            <td style={tdStyle}>{job.jobCredit}</td>
                            <td style={tdStyle}>{job.jobCurrent} / {job.jobEnrolled} / {job.jobRequired}</td>
                            <td style={tdStyle}>
                                {getStatusButton(job.jobId)}
                            </td>
                        </tr>
                        {selectedJobIds.has(job.jobId) && (
                            <tr style={trStyle}>
                                <td colSpan={5} style={tdStyle}>
                                    <strong>Long Description:</strong> {job.jobLongDescription}
                                </td>
                            </tr>
                        )}
                    </React.Fragment>
                ))}
                </tbody>
            </table>
            {selectedJobId !== null && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Are you sure you want to enroll in this job?</h3>
                        <button onClick={confirmEnroll} style={confirmButtonStyle}>Yes</button>
                        <button onClick={() => setSelectedJobId(null)} style={cancelButtonStyle}>No</button>
                    </div>
                </div>
            )}
            <button onClick={toMyCenter} style={backButtonStyle}>Back to My Center</button>
            <ErrorModal message={error} onClose={()=>setError(null)}/>
        </StudentLayout>
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

const enrollButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'pointer',
};

const pendingButtonStyle: React.CSSProperties = {
    background: '#ffcc00',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'not-allowed',
};

const approvedButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'default',
};

const rejectedButtonStyle: React.CSSProperties = {
    background: '#ff6347',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'default',
};

const submitButtonStyle: React.CSSProperties = {
    background: '#007bff',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'pointer',
};

const submittedButtonStyle: React.CSSProperties = {
    background: '#8a2be2',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'default',
};

const passButtonStyle: React.CSSProperties = {
    background: '#4CAF50',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'default',
};

const failButtonStyle: React.CSSProperties = {
    background: '#f44336',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'default',
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

const backButtonStyle: React.CSSProperties = {
    marginTop: '20px',
    padding: '10px 20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s',
};

