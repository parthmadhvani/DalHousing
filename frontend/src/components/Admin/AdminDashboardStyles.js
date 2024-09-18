import styled from "styled-components";

export const Tabs = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
`;

export const Tab = styled.button`
    background-color: #f1f1f1;
    
    border: 1px solid #ccc;
    padding: 10px 20px;
    margin: 0 5px;
    cursor: pointer;
    transition: background-color 0.3s;
`;

export const Container = styled.div``;

export const CardContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
`;

export const Card = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  border: 1px solid #ddd;
  border-radius: 5px;
  overflow: hidden;
`;

export const CardContent = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 15px; /* Increased padding for spacing between data and card border */
`;

export const CardDetails = styled.div`
  flex: 1;
  padding-right: 15px; /* Ensure the padding matches the button gap */

  .status-approved {
    font-weight: bold;
    font-size: 18px; /* Larger font size */
    font-style: italic;
    color: #28a745; /* Green color for approved status */
  }

  .status-rejected {
    font-weight: bold;
    font-size: 18px; /* Larger font size */
    font-style: italic;
    color: #dc3545; /* Red color for rejected status */
  }

  .status-pending {
    font-weight: bold;
    font-size: 18px; /* Larger font size */
    font-style: italic;
    color: #17a2b8; /* Blue color for pending status */
  }

  .status-all {
    font-weight: bold;
    font-size: 18px; /* Larger font size */
    font-style: italic;
    color: #6c757d; /* Gray color for all other statuses */
  }
`;

export const ShortDetails = styled.div`
  ul {
    list-style-type: disc;
    margin: 10px;
    padding: 10px;
    display: flex;
    flex-wrap: wrap;
  }
  li {
    width: 50%;
  }
`;

export const LongDetails = styled.div`
  ul {
    list-style-type: disc;
    margin: 10px;
    padding: 10px;
  }
`;

export const ButtonGroup = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 15px; /* Increased gap for better spacing */

  button {
    padding: 14px 24px; /* Increased padding for larger buttons */
    font-size: 16px; /* Increased font size for better readability */
    border: 1px solid #ccc;
    border-radius: 8px; /* Rounded corners for buttons */
    cursor: pointer;
    transition: background-color 0.3s, box-shadow 0.3s;
    width: 180px; /* Set a fixed width for uniform button size */

    &:disabled {
      background-color: #e2e6ea;
      cursor: not-allowed;
    }

    &:not(:disabled):hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Added shadow for hover effect */
    }
  }

  .btn-success {
    background-color: #28a745;
    color: white;

    &:hover {
      background-color: #2ea043; /* Slightly lighter green on hover */
    }
  }

  .btn-danger {
    background-color: #dc3545;
    color: white;

    &:hover {
      background-color: #c82333; /* Slightly lighter red on hover */
    }
  }

  .btn-info {
    background-color: #17a2b8;
    color: white;

    &:hover {
      background-color: #138496; /* Slightly lighter blue on hover */
    }
  }
`;
