//  import './App.css';
// import React from 'react';
// import {Routes, Route } from 'react-router-dom';
// import Signup from './components/auth/Signup';
// import Login from './components/auth/Login';
// import Logout from './components/auth/Logout'
// import ChangePassword from './components/auth/ChangePassword';
// import ForgotPassword from './components/auth/ForgotPassword';
// import ResetPassword from './components/auth/ResetPassword';
// import GoogleAPI from './components/properties/GoogleAPI';
// import PropertyTable from './components/dashboard/PropertyTable';
// import RemoveAccount from './components/dashboard/RemoveAccount.js';
// import LandingPage from './components/LandingPage';
// import PropertyDetails from './components/properties/PropertyDetails.js';
// import MapComponent from './components/properties/MapComponent.js';
// import SavedProperties from './components/property/SavedProperties';
// import PropertyList from './components/property/PropertyList';
// import 'bootstrap/dist/css/bootstrap.min.css';

// function App() 
// {
//   const edit_properties_path = "/edit_properties/:property_id"
//   const add_properties_path = "/add_properties";
//   const dashboard_path = "/dashboard/:id";
//   const remove_account_path = "/remove_account/:id";
//   const view_property_path = "/view_property/:id";
//   const show_map_path = "/show_map/:id";

  
//   return (
        
//         <Routes>
//           <Route path="/" element={<LandingPage />} />
//           <Route path="/login" element={<Login />} />
//           <Route path="/signup" element={<Signup />} />
//           <Route path= "/change-password" element={<ChangePassword />} />
//           <Route path="/forgot-password" element={<ForgotPassword />} />
//           <Route path="/reset-password" element={<ResetPassword />} />
//           <Route path="/logout" element={<Logout />} />
//           <Route path={add_properties_path} element={<GoogleAPI componentToRender={"Property_form"} />} />
//           <Route path={edit_properties_path} element={<GoogleAPI componentToRender={"Edit_properties"} />} />
//           <Route path={dashboard_path} element={<PropertyTable />} />
//           <Route path={remove_account_path} element={<RemoveAccount />} />
//           <Route path={view_property_path} element={<PropertyDetails />} />
//           <Route path={show_map_path} element={<MapComponent />} />
//           <Route path="/search-properties" element={<PropertyList />} />
//           <Route path="/saved-properties" element={<SavedProperties />} />

//         </Routes>
//   );
// }
// export default App;


import './App.css';
import React from 'react';
import {Routes, Route } from 'react-router-dom';
import Signup from './components/auth/Signup';
import Login from './components/auth/Login';
import Logout from './components/auth/Logout'
import ChangePassword from './components/auth/ChangePassword';
import ForgotPassword from './components/auth/ForgotPassword';
import ResetPassword from './components/auth/ResetPassword';
import GoogleAPI from './components/properties/GoogleAPI';
import PropertyTable from './components/dashboard/PropertyTable';
import RemoveAccount from './components/dashboard/RemoveAccount.js';
import LandingPage from './components/LandingPage';
import PropertyDetails from './components/properties/PropertyDetails.js';
import MapComponent from './components/properties/MapComponent.js';
import SavedProperties from './components/property/SavedProperties';
import PropertyList from './components/property/PropertyList';
import Admindashboard from "./components/Admin/AdminDashboard.js";
import 'bootstrap/dist/css/bootstrap.min.css';

function App()
{
  const edit_properties_path = "/edit_properties"
  const add_properties_path = "/add_properties";
  const dashboard_path = "/dashboard";
  const remove_account_path = "/remove_account/:id";
  const view_property_path = "/view_property/:id";
  const show_map_path = "/show_map/:id";


  return (

        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path= "/change-password" element={<ChangePassword />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/reset-password" element={<ResetPassword />} />
          <Route path="/logout" element={<Logout />} />
          <Route path={add_properties_path} element={<GoogleAPI componentToRender={"Property_form"} />} />
          <Route path={edit_properties_path} element={<GoogleAPI componentToRender={"Edit_properties"} />} />
          <Route path={dashboard_path} element={<PropertyTable />} />
          <Route path={remove_account_path} element={<RemoveAccount />} />
          <Route path={view_property_path} element={<PropertyDetails />} />
          <Route path={show_map_path} element={<MapComponent />} />
          <Route path="/search-filter" element={<PropertyList />} />
          {/* <Route path="/search-properties" element={<PropertyList />} /> */}
          <Route path="/saved-properties" element={<SavedProperties />} />
            <Route path="/Admindashboard" element={<Admindashboard />} />

        </Routes>
  );
}
export default App;
