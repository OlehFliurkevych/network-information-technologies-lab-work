//import React, { useEffect, useState } from 'react';
//import { Button, ButtonGroup, Container, Table } from 'reactstrap';
//import AppNavbar from './AppNavbar';
//import { Link } from 'react-router-dom';
//import { useCookies } from 'react-cookie';
//
//const RestaurantList = () => {
//
//  const [restaurants, setRestaurants] = useState([]);
//  const [loading, setLoading] = useState(false);
//  const [cookies] = useCookies(['XSRF-TOKEN']);
//
//  useEffect(() => {
//    setLoading(true);
//
//    fetch('api/restaurants')
//      .then(response => response.json())
//      .then(data => {
//        setRestaurants(data);
//        setLoading(false);
//      })
//  }, []);
//
//  const remove = async (id) => {
//    await fetch(`/api/restaurants/${id}`, {
//      method: 'DELETE',
//      headers: {
//        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
//        'Accept': 'application/json',
//        'Content-Type': 'application/json'
//      },
//      credentials: 'include'
//    }).then(() => {
//      let updatedRestaurants = [...restaurants].filter(i => i.id !== id);
//      setRestaurants(updatedRestaurants);
//    });
//  }
//
//  if (loading) {
//    return <p>Loading...</p>;
//  }
//
//  const restaurantList = restaurants.map(restaurant => {
//   return <tr key={restaurant.id}>
//      <td style={{ whiteSpace: 'nowrap' }}>{restaurant.name}</td>
//      <td style={{ whiteSpace: 'nowrap' }}>{restaurant.address}</td>
//      <td>
//        <ButtonGroup>
//          <Button size="sm" color="primary" tag={Link} to={"/restaurants/" + restaurant.id}>Edit</Button>
//          <Button size="sm" color="danger" onClick={() => remove(restaurant.id)}>Delete</Button>
//        </ButtonGroup>
//      </td>
//    </tr>
//  });
//
//  return (
//    <div>
//      <AppNavbar/>
//      <Container fluid>
//        <div className="float-end">
//          <Button color="success" tag={Link} to="/restaurants/new">Add Restaurant</Button>
//        </div>
//        <h3>My JUG Tour</h3>
//        <Table className="mt-4">
//          <thead>
//          <tr>
//            <th width="20%">Name</th>
//            <th width="20%">Location</th>
//            <th>Events</th>
//            <th width="10%">Actions</th>
//          </tr>
//          </thead>
//          <tbody>
//          {restaurantList}
//          </tbody>
//        </Table>
//      </Container>
//    </div>
//  );
//};
//
//export default RestaurantList;
