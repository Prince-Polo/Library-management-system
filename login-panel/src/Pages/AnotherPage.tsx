import React, { useState, useCallback, useEffect } from 'react';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import AdminLayout from './AdminLayout';

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

interface ApprovedJobQueryMessage {
    type: string;
}

interface CheckTaskMessage {
    jobId: number;
    studentId: string;
}

const TasksPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [selectedJobId, setSelectedJobId] = useState<number | null>(null);
    const studentId = "333"; // 假设的学生ID

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

            let result = await response.text(); // Fetch as text
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                parsedResult = JSON.parse(parsedResult);
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

    useEffect(() => {
        fetchJobs();
    }, [fetchJobs]);

    const handleEnrollClick = (jobId: number) => {
        setSelectedJobId(jobId);
    };

    const confirmEnroll = async () => {
        if (selectedJobId === null) return;

        const checkTaskMessage: CheckTaskMessage = {
            jobId: selectedJobId,
            studentId
        };

        try {
            // 发送请求检查任务表中是否存在相同的 studentId
            const checkResponse = await fetch('http://127.0.0.1:10004/api/Job/CheckTask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(checkTaskMessage)
            });

            const checkResult = await checkResponse.json();
            console.log("Check Result:", checkResult);

            if (checkResult) {
                setError('You are already enrolled in this job');
                setSelectedJobId(null);
                return;
            }

            // 如果不存在相同的 studentId，则继续进行更新和创建任务的操作
            const updateJobMessage = {
                type:'UpdateJobCurrentMessage',
                jobId: selectedJobId,
                increment: 1
            };

            const createTaskMessage = {
                type:'CreateTaskMessage',
                jobId: selectedJobId,
                studentId
            };

            // Send request to update job
            await fetch('http://127.0.0.1:10004/api/Job/UpdateJobCurrentMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updateJobMessage)
            });
            console.log(updateJobMessage)

            // Send request to create task
            await fetch('http://127.0.0.1:10004/api/Job/CreateTaskMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(createTaskMessage)
            });
            console.log(createTaskMessage)

            fetchJobs();

        } catch (error) {
            setError('Failed to enroll in job');
            console.error('Error enrolling in job:', error);
        }

        setSelectedJobId(null);
    };

    return (
        <AdminLayout>
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
                {jobs.map((job: Job) => (
                    <React.Fragment key={job.jobId}>
                        <tr style={trStyle}>
                            <td style={tdStyle}>{job.jobShortDescription}</td>
                            <td style={tdStyle}>{job.jobHardness}</td>
                            <td style={tdStyle}>{job.jobCredit}</td>
                            <td style={tdStyle}>{job.jobCurrent} / {job.jobEnrolled} / {job.jobRequired}</td>
                            <td style={tdStyle}>
                                <button onClick={() => handleEnrollClick(job.jobId)} style={enrollButtonStyle}>Enroll</button>
                            </td>
                        </tr>
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

const enrollButtonStyle: React.CSSProperties = {
    background: '#32cd32',
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

export default TasksPage;

