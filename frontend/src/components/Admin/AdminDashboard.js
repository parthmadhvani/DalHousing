import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../layout/AdminHeader";
import axios from "axios";
import {
  Tabs,
  Tab,
  Container,
  CardContainer,
  Card,
  CardContent,
  CardDetails,
  ShortDetails,
  LongDetails,
  ButtonGroup
} from "./AdminDashboardStyles";


const AdminDashboard = () => {
  //const [properties, setProperties] = useState([]);
  const [filteredProperties, setFilteredProperties] = useState({
    all: [],
    approved: [],
    rejected: [],
    pending: []
  });
  const [pagination, setPagination] = useState({
    currentPageNumber: 0,
    currentPageSize: 6,
    totalProperties: 0,
    totalPages: 1,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState("all");
  useNavigate();

  useEffect(() => {
    const fetchProperties = async () => {
      setLoading(true);
      try {

        const token = localStorage.getItem('token');

        const response = await axios.get(`http://172.17.0.139:8080/admin/properties`, {
          params: {
            pageNumber: pagination.currentPageNumber,
            pageSize: pagination.currentPageSize,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const allProperties = response.data;

        // Normalize statuses to lowercase
        const groupedProperties = allProperties.reduce((acc, property) => {
          const statusLowerCase = property.status.toLowerCase();
          if (!acc[statusLowerCase]) acc[statusLowerCase] = [];
          acc[statusLowerCase].push(property);
          return acc;
        }, {});

        setFilteredProperties({
          all: allProperties,
          approved: groupedProperties["approved"] || [],
          rejected: groupedProperties["rejected"] || [],
          pending: groupedProperties["pending"] || []
        });

        setPagination({
          currentPageNumber: response.data.currentPageNumber,
          currentPageSize: response.data.currentPageSize,
          totalProperties: response.data.totalProperties,
          totalPages: response.data.totalPages,
        });
      } catch (error) {
        setError("Error fetching properties.");
        console.error("Error fetching properties:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProperties();
  }, [pagination.currentPageNumber, pagination.currentPageSize]);

  const handleApproveReject = async (id, status) => {
    try {

      const token = localStorage.getItem('token');
      // const statusUpperCase = status.toUpperCase(); // Ensure status is in uppercase
      const response = await axios.post(`http://172.17.0.139:8080/admin/properties/${id}`, {
        status: status
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      // console.log(updatedProperty);
      const updatedProperty = response.data;
      setFilteredProperties((prev) => {
        const updated = { ...prev };
        Object.keys(updated).forEach((key) => {
          updated[key] = updated[key].map((property) =>
            property.id === id ? updatedProperty : property
          );
        });
        return updated;
      });
      alert(`Property ${status} successfully!`);
    } catch (error) {
      console.error("Error updating property status:", error);
      alert(`Error updating property status: ${error.message}`);
    }
  };

  const handleEmailVerification = async (id) => {
    try {
      const token = localStorage.getItem('token');

      await axios.post(`http://172.17.0.139:8080/admin/properties/${id}/verify`, {}, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }); alert("Verification email sent successfully!");
    } catch (error) {
      console.error("Error sending email verification:", error);
      alert(`Error sending email verification: ${error.message}`);
    }
  };

  const handleStatusChange = (status) => {
    setSelectedStatus(status.toLowerCase()); // Convert to lowercase for consistency
  };

  return (
    <div>
      <Header />
      <Tabs>
        {["all", "approved", "rejected", "pending"].map((status) => (
          <Tab
            key={status}
            className={selectedStatus === status ? "active" : ""}
            onClick={() => handleStatusChange(status)}
          >
            {status}
          </Tab>
        ))}
      </Tabs>
      <Container>
        <h2 className="text-center">Property List</h2>
        {loading ? (
          <p>Loading...</p>
        ) : error ? (
          <p>Error: {error}</p>
        ) : (
          <>
            <CardContainer>
              {filteredProperties[selectedStatus].map((property) => (
                <Card className="card" key={property.id}>
                  <CardContent>
                    <CardDetails>
                      <h5 className="card-title">{property.property_heading}</h5>
                      <ShortDetails>
                        <ul>
                          <li>Type: {property.property_type}</li>
                          <li>Furnishing: {property.furnishing}</li>
                          <li>Bedrooms: {property.bedrooms}</li>
                          <li>Bathrooms: {property.bathrooms}</li>
                          <li>Monthly Rent: ${property.monthly_rent}</li>
                          <li>Parking: {property.parking}</li>
                          <li>Availability: {new Date(property.availability).toLocaleString()}</li>
                          <li>Location: {property.street_address}, {property.city}, {property.province}, {property.postal_code}</li>
                          <li>Latitude: {property.latitude}</li>
                          <li>Longitude: {property.longitude}</li>
                          <li>Property ID: {property.id}</li>
                          <li>Owner ID: {property.owner_id}</li>
                          <li>Owner Email: {property.email}</li>
                          <li>Mobile: {property.mobile}</li>
                          <li className={`status-${property.status.toLowerCase()}`}>
                            Status: {property.status}
                          </li>
                        </ul>
                      </ShortDetails>
                      <LongDetails>
                        <ul>
                          <li>Description: {property.full_description}</li>
                          <li>YouTube Link: {property.youtube}</li>
                        </ul>
                      </LongDetails>
                    </CardDetails>
                    <ButtonGroup>
                      {(selectedStatus === "pending" || selectedStatus === "all") && (
                        <>
                          <button
                            onClick={() => handleApproveReject(property.id, "Approved")}
                            className="btn btn-success"
                          >
                            Approve
                          </button>
                          <button
                            onClick={() => handleApproveReject(property.id, "Rejected")}
                            className="btn btn-danger"
                          >
                            Reject
                          </button>
                          <button
                            onClick={() => handleEmailVerification(property.id)}
                            className="btn btn-info"
                          >
                            Verify
                          </button>
                        </>
                      )}
                    </ButtonGroup>
                  </CardContent>
                </Card>
              ))}
            </CardContainer>
          </>
        )}
      </Container>
    </div>
  );
};

export default AdminDashboard;
