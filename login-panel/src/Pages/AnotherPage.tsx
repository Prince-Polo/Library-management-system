import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router';
import { ApprovedJobQueryMessage } from 'Plugins/JobAPI/ApprovedJobQueryMessage';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import './app.css';

interface JobInfo {
    jobid: string;
    jobstudentid: string;
    jobshortdescription: string;
    joblongdescription: string;
    jobhardness: string;
    jobcredit: string;
    jobcomplete: boolean;
    jobbooked: boolean;
    jobapproved: boolean;
}

const ApprovedJobQuery: React.FC = () => {
    const [jobs, setJobs] = useState<JobInfo[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const history = useHistory();

    const fetchJobs = () => {
        const message = new ApprovedJobQueryMessage();
        sendPostRequest(message, setError, (response: any) => {
            console.log('Received response:', response);
            try {
                const jobs: JobInfo[] = JSON.parse(response); // 解析返回的字符串为JSON对象
                setJobs(jobs);
                setSuccess("Jobs fetched successfully");
            } catch (err) {
                setError("Failed to parse jobs data");
            }
        });
    };

    useEffect(() => {
        fetchJobs();
    }, []);

    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    return (
        <div className="job-query-container">
            <div className="job-query">
                <h2>Approved Job Query</h2>
                <button className='button button-primary' onClick={fetchJobs}>Fetch Approved Jobs</button>
                <div className="job-list">
                    {jobs.map(job => (
                        <div className="job-item" key={job.jobid}>
                            <h3>{job.jobshortdescription}</h3>
                            <p><strong>Student ID:</strong> {job.jobstudentid}</p>
                            <p><strong>Description:</strong> {job.joblongdescription}</p>
                            <p><strong>Hardness:</strong> {job.jobhardness}</p>
                            <p><strong>Credit:</strong> {job.jobcredit}</p>
                            <p><strong>Complete:</strong> {job.jobcomplete ? 'Yes' : 'No'}</p>
                            <p><strong>Booked:</strong> {job.jobbooked ? 'Yes' : 'No'}</p>
                            <p><strong>Approved:</strong> {job.jobapproved ? 'Yes' : 'No'}</p>
                        </div>
                    ))}
                </div>
                <button
                    type="button"
                    className='button button-primary'
                    onClick={() => history.push("/")}
                >
                    Return to first page
                </button>
            </div>
            <ErrorModal message={error} onClose={closeModal} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

// 将 ApprovedJobQueryPage 作为默认导出
const ApprovedJobQueryPage: React.FC = () => {
    return (
        <div style={{ background: 'lightgrey', opacity: '90%' }}>
            <ApprovedJobQuery />
        </div>
    );
};

export default ApprovedJobQueryPage;
