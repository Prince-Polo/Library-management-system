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
    jobEnrolled: number; // 新增字段
    jobRequired: number;
}

interface ApprovedJobQueryMessage {
    type: string;
}

interface DeleteJobMessage {
    jobId: number;
}

const BrowseJobsPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [approvedJobs, setApprovedJobs] = useState<Job[]>([]);
    const [selectedJobIds, setSelectedJobIds] = useState<Set<number>>(new Set<number>());
    const [jobToDelete, setJobToDelete] = useState<number | null>(null);

    const fetchApprovedJobs = useCallback(async () => {
        setError(null);

        const message: ApprovedJobQueryMessage = {
            type: 'ApprovedJobQuery'
        };
        console.log(message);

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Job/ApprovedJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            let result = await response.text(); // Fetch as text
            console.log('Response Data:', result);

            // 解析字符串为 JSON 对象
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                console.log('First Parsed Response Data:', parsedResult);
                parsedResult = JSON.parse(parsedResult);
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }
            console.log('Second Parsed Response Data:', parsedResult);

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
                jobEnrolled: job.jobEnrolled, // 新增字段
                jobRequired: job.jobRequired
            }));
            setApprovedJobs(jobs);
            console.log('Parsed jobs:', jobs);

        } catch (error) {
            setError('Failed to fetch approved jobs');
            console.error('Error fetching approved jobs:', error);
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
            }
            return newSelectedJobIds;
        });
    };

    const handleDeleteJob = (jobId: number) => {
        setJobToDelete(jobId);
    };

    const confirmDeleteJob = async () => {
        if (jobToDelete === null) return;

        setError(null);

        const message: DeleteJobMessage = {
            jobId: jobToDelete
        };
        console.log(message);

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Job/DeleteJobMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            if (!response.ok) {
                const result = await response.json();
                setError(result.error || 'Failed to delete job');
            }

            fetchApprovedJobs();

        } catch (error) {
            setError('Failed to delete job');
            console.error('Error deleting job:', error);
        }

        setJobToDelete(null);
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
                    <th style={thStyle}>Current / Enrolled / Required</th> {/* 修改标题 */}
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
                            <td style={tdStyle}>{job.jobCurrent} / {job.jobEnrolled} / {job.jobRequired}</td> {/* 修改显示内容 */}
                            <td style={tdStyle}>
                                <button onClick={() => handleDeleteJob(job.jobId)} style={deleteButtonStyle}>Delete</button>
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
            {jobToDelete !== null && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Are you sure you want to delete this job?</h3>
                        <button onClick={confirmDeleteJob} style={confirmButtonStyle}>Yes</button>
                        <button onClick={() => setJobToDelete(null)} style={cancelButtonStyle}>No</button>
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

export default BrowseJobsPage;
