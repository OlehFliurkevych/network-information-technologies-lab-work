import React from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import GroupList from './GroupList';
import GroupEdit from './GroupEdit';
import RestaurantList from './RestaurantList';
import RestaurantEdit from './RestaurantEdit';
import TableList from './TableList';
import TableEdit from './TableEdit';
import EventList from './EventList';
import EventEdit from './EventEdit';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Home/>}/>
        <Route path="/groups" exact={true} element={<GroupList/>}/>
        <Route path="/groups/:id" exact={true} element={<GroupEdit/>}/>
        <Route path="/restaurants" exact={true} element={<RestaurantList/>}/>
        <Route path="/restaurants/:id" exact={true} element={<RestaurantEdit/>}/>
        <Route path="/tables" exact={true} element={<TableList/>}/>
        <Route path="/tables/:id" exact={true} element={<TableEdit/>}/>
        <Route path="/events" exact={true} element={<EventList/>}/>
        <Route path="/events/:id" exact={true} element={<EventEdit/>}/>
      </Routes>
    </Router>
  )
}

export default App;
