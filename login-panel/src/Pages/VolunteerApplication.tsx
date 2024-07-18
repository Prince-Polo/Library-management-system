import React, { useState, useEffect, useCallback } from 'react';
import { useHistory } from 'react-router-dom';
import StudentLayout from './StudentLayout';
import { useStore } from './store';
import {useToMyCenter} from 'Pages/LibraryDataLinking'

interface Job {
    jobId: number;
    jobShortDescription: string;
    jobLongDescription: string;
    jobHardness: number;
    jobCredit: number;
    jobCurrent: number;
    jobEnrolled: number;
    jobRequired: number;
}

interface Task {
    jobId: number;
    status: number;
}

interface ApprovedJobQueryMessage {
    type: string;
}

const VolunteerApplication: React.FC = () => {
    const {toMyCenter,err:clearToMyCenterError}=useToMyCenter()
    const [error, setError] = useState<string | null>(null);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [selectedJobIds, setSelectedJobIds] = useState<Set<number>>(new Set<number>());
    const [selectedJobId, setSelectedJobId] = useState<number | null>(null);

    const token = useStore((state) => state.info.token);
    const history = useHistory();

    const fetchJobs = useCallback(async () => {
        setError(null);

        const message: ApprovedJobQueryMessage = {
            type: 'ApprovedJobQuery'
        };

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Job/ApprovedJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            let result = await response.text();
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                parsedResult = JSON.parse(parsedResult); // 第二次解析
                console.log('approve',parsedResult)
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }

            if (!Array.isArray(parsedResult)) {
                throw new Error('Invalid data format received: expected an array');
            }

            const jobs = parsedResult.map((job: any) => ({
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

        } catch (error) {
            setError('Failed to fetch jobs');
            console.error('Error fetching jobs:', error);
        }
    }, []);

    const fetchTasks = useCallback(async () => {
        setError(null);

        const message = { token };

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Student/StudentSpecificAcceptedJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            let result = await response.text();
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                if (typeof parsedResult === 'string') {
                    parsedResult = JSON.parse(parsedResult); // 第二次解析
                }
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }
            console.log(parsedResult)

            if (!Array.isArray(parsedResult.tasks)) {
                throw new Error('Invalid data format received: expected an array');
            }

            const tasks = parsedResult.tasks.map((task: any) => ({
                jobId: task.jobId,
                status: task.status
            }));
            setTasks(tasks);

        } catch (error) {
            setError('Failed to fetch tasks');
            console.error('Error fetching tasks:', error);
        }
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

        const updateJobMessage = {
            type: 'UpdateJobCurrentMessage',
            jobId: selectedJobId,
            increment: 1
        };

        const createTaskMessage = {
            type: 'CreateTaskMessage',
            jobId: selectedJobId,
            token: token // Use token from store
        };

        try {
            // Send request to update job
            await fetch('http://127.0.0.1:10004/api/Job/UpdateJobCurrentMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updateJobMessage)
            });

            // Send request to create task
            await fetch('http://127.0.0.1:10004/api/Student/AcceptJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(createTaskMessage)
            });

            fetchJobs();
            fetchTasks();

        } catch (error) {
            setError('Failed to enroll in job');
            console.error('Error enrolling in job:', error);
        }

        setSelectedJobId(null);
    };

    const submitTask = async (jobId: number) => {
        const submitTaskMessage = {
            type: 'SubmitTaskMessage',
            jobId: jobId,
            token: token // Use token from store
        };

        try {
            await fetch('http://127.0.0.1:10004/api/Student/SubmitJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(submitTaskMessage)
            });

            await fetchTasks();

            // 检查是否还有状态为1的任务
            const hasActiveTasks = tasks.some(task => task.jobId !== jobId &&task.status === 1);
            console.log("hasActive",hasActiveTasks)

            // 如果没有状态为1的任务，更新志愿者状态为false
            if (!hasActiveTasks) {
                const updateVolunteerStatusMessage = {
                    type: 'UpdateVolunteerStatusMessage',
                    token: token // Assuming token is the student ID or you might need to decode it to get the student ID
                };

                await fetch('http://127.0.0.1:10004/api/Student/VolunteerStatusFalseUsingTokenMessage', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updateVolunteerStatusMessage)
                });
            }

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

export default VolunteerApplication;
