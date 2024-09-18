import React, { useEffect, useState, useCallback } from "react";
import {
  getProperties,
  searchProperties,
  saveProperty,
  getCities,
  getPropertyTypes,
} from "../../service/Api";
import Filter from "../../layout/Filter";
import Header from "../../layout/Header";
import { useNavigate } from "react-router-dom";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Button,
  CardActions,
  MenuItem,
  Select,
  Box,
} from "@mui/material";
import { styled } from "@mui/system";
import { io } from "socket.io-client";
import "../../style/PropertyList.css";

const StyledCard = styled(Card)(({ theme }) => ({
  maxWidth: 345,
  margin: theme.spacing(2),
}));

const StyledCardMedia = styled(CardMedia)(({ theme }) => ({
  height: 140,
}));

const StyledCardActions = styled(CardActions)(({ theme }) => ({
  justifyContent: "space-between",
}));

const ActionButton = styled(Button)(({ color }) => ({
  backgroundColor: color,
  color: "#fff",
  "&:hover": {
    backgroundColor: color,
  },
}));

const SortControls = styled(Box)(({ theme }) => ({
  marginBottom: theme.spacing(2),
}));

const Pagination = styled(Box)(({ theme }) => ({
  display: "flex",
  justifyContent: "center",
  marginTop: theme.spacing(2),
  "& button": {
    margin: theme.spacing(0.5),
  },
  "& .active": {
    fontWeight: "bold",
  },
}));

const PropertyCard = ({ property, onApprove, onReject, onChat }) => {
  const handleImageError = (e) => {
    e.target.src = "/default/image.jpg";
  };

  return (
    <StyledCard>
      <StyledCardMedia
        image={property.imageUrls[0] || "/default/image.jpg"}
        title={`Property in ${property.city}`}
        onError={handleImageError}
      />
      <CardContent>
        <Typography variant="h5" component="div">
          $ {property.monthly_rent}
        </Typography>
        <Typography variant="body2" color="textSecondary" component="p">
          {property.street_address}
        </Typography>
        <Typography variant="body2" color="textSecondary" component="p">
          {property.city}
        </Typography>
        <Typography variant="body2" color="textSecondary" component="p">
          {property.property_type}
        </Typography>
        <Typography variant="body2" color="textSecondary" component="p">
          {property.bedrooms} Bedrooms, {property.bathrooms} Bathrooms
        </Typography>
      </CardContent>
      <StyledCardActions>
        <ActionButton
          size="small"
          color="#4caf50"
          onClick={() => onApprove(property.id)}
        >
          Approve
        </ActionButton>
        <ActionButton
          size="small"
          color="#f44336"
          onClick={() => onReject(property.id)}
        >
          Reject
        </ActionButton>
        <ActionButton
          size="small"
          color="#2196f3"
          onClick={() => onChat(property.userId)}
        >
          Chat
        </ActionButton>
      </StyledCardActions>
    </StyledCard>
  );
};

const PropertyList = () => {
  const [properties, setProperties] = useState([]);
  const [cities, setCities] = useState([]);
  const [pagination, setPagination] = useState({
    currentPageNumber: 0,
    currentPageSize: 6,
    totalProperties: 0,
    totalPages: 0,
  });
  const [filters, setFilters] = useState({
    city: "",
    property_type: "",
    min_price: "",
    max_price: "",
    bedrooms: "",
    bathrooms: "",
    furnishing: "",
    parking: "",
    pageNumber: 0,
    pageSize: 6,
    sortBy: "id",
    sortDir: "",
  });
  const navigate = useNavigate();
  const [propertyTypes, setPropertyTypes] = useState([]);
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    // Adding some dummy data
    setProperties([
      {
        id: 1,
        imageUrls: ["/path/to/image1.jpg"],
        monthly_rent: 1200,
        street_address: "123 Main St",
        city: "New York",
        property_type: "Apartment",
        bedrooms: 2,
        bathrooms: 1,
        userId: 1,
      },
      {
        id: 2,
        imageUrls: ["/path/to/image2.jpg"],
        monthly_rent: 2500,
        street_address: "456 Oak St",
        city: "Los Angeles",
        property_type: "House",
        bedrooms: 3,
        bathrooms: 2,
        userId: 2,
      },
      {
        id: 3,
        imageUrls: ["/path/to/image3.jpg"],
        monthly_rent: 800,
        street_address: "789 Pine St",
        city: "Chicago",
        property_type: "Studio",
        bedrooms: 1,
        bathrooms: 1,
        userId: 3,
      },
    ]);
  }, []);

  useEffect(() => {
    const socketInstance = io("http://172.17.0.139:4000"); // Replace with your backend WebSocket URL
    setSocket(socketInstance);

    socketInstance.on("newProperty", (newProperty) => {
      setProperties((prevProperties) => [newProperty, ...prevProperties]);
    });

    return () => {
      socketInstance.disconnect();
    };
  }, []);

  const handleFilterChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value, pageNumber: 0 });
  };

  const handleSearch = async (keyword) => {
    if (keyword.trim() === "") {
      setProperties();
      return;
    }
    try {
      const response = await searchProperties(keyword, {
        ...filters,
        status: "Approved",
      });
      setProperties(response.data.properties);
      setPagination({
        currentPageNumber: response.data.currentPageNumber,
        currentPageSize: response.data.currentPageSize,
        totalProperties: response.data.totalProperties,
        totalPages: response.data.totalPages,
      });
    } catch (error) {
      console.error("Failed to search properties", error);
    }
  };

  const handleSave = async (id) => {
    try {
      const userId = 1; // For testing
      await saveProperty(userId, id);
      alert("Property saved successfully!");
    } catch (error) {
      console.error("Failed to save property", error);
      alert("Failed to save property");
    }
  };

  const handleView = (id) => {
    navigate(`/view_property/${id}`, { state: { property_id: id } });
  };

  const handleApprove = (propertyId) => {
    fetch(`/api/properties/${propertyId}/approve`, { method: "POST" })
      .then((response) => response.json())
      .then((data) => {
        console.log("Property approved:", data);
        setProperties(); // Refresh properties after approval
      })
      .catch((error) => {
        console.error("Error approving property:", error);
      });
  };

  const handleReject = (propertyId) => {
    fetch(`/api/properties/${propertyId}/reject`, { method: "POST" })
      .then((response) => response.json())
      .then((data) => {
        console.log("Property rejected:", data);
        setProperties(); // Refresh properties after rejection
      })
      .catch((error) => {
        console.error("Error rejecting property:", error);
      });
  };

  const handleChat = (userId) => {
    window.location.href = `/chat/${userId}`;
  };

  const handlePageChange = (newPage) => {
    setFilters({ ...filters, pageNumber: newPage });
  };

  const handleSortChange = (e) => {
    const value = e.target.value;
    setFilters({
      ...filters,
      sortBy: "monthly_rent",
      sortDir: value,
    });
  };

  return (
    <div>
      <Header />
      <Filter
        onFilterChange={handleFilterChange}
        onSearch={handleSearch}
        cities={cities}
        propertyTypes={propertyTypes}
      />
      <SortControls>
        <Select
          value={filters.sortDir}
          onChange={handleSortChange}
          displayEmpty
        >
          <MenuItem value="">Sort by</MenuItem>
          <MenuItem value="asc">Price: Low to High</MenuItem>
          <MenuItem value="desc">Price: High to Low</MenuItem>
        </Select>
      </SortControls>
      <div className="property-list">
        {properties.length === 0 ? (
          <Typography variant="body1">No properties found.</Typography>
        ) : (
          properties.map((property) => (
            <PropertyCard
              key={property.id}
              property={property}
              onApprove={handleApprove}
              onReject={handleReject}
              onChat={handleChat}
            />
          ))
        )}
      </div>
      <Pagination>
        {Array.from({ length: pagination.totalPages }, (_, index) => (
          <Button
            key={index}
            onClick={() => handlePageChange(index)}
            className={index === pagination.currentPageNumber ? "active" : ""}
          >
            {index + 1}
          </Button>
        ))}
      </Pagination>
    </div>
  );
};

export default PropertyList;
