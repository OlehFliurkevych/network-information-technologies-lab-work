import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { useCookies } from 'react-cookie';

const RestaurantEdit = () => {
  const initialFormState = {
    name: '',
    address: '',
  };
  const [restaurant, setRestaurant] = useState(initialFormState);
  const navigate = useNavigate();
  const { id } = useParams();
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id !== 'new') {
      fetch(`/api/restaurants/${id}`)
        .then(response => response.json())
        .then(data => setRestaurant(data));
    }
  }, [id, setRestaurant]);

  const handleChange = (event) => {
    const { name, value } = event.target

    setRestaurant({ ...restaurant, [name]: value })
  }

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await fetch(`/api/restaurants${restaurant.id ? `/${restaurant.id}` : ''}`, {
            method: (restaurant.id) ? 'PUT' : 'POST',
            headers: {
              'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(restaurant),
            credentials: 'include'
          });
      if (!response.ok) {
        console.log(response);
        const dateJson = await response.json()
        console.log("Response");
        console.log(dateJson);
        console.log("Response after");
        throw new Error(dateJson.message);
      }
    setRestaurant(initialFormState);
    navigate('/restaurants');
    } catch (error){
          setError(error.message);
          console.log(error);
          return error;
    }
  }

  const title = <h2>{restaurant.id ? 'Edit Restaurant' : 'Add Restaurant'}</h2>;

  const renderError = () => {
    return error ? (
      <div className="alert alert-danger" role="alert">
        {error}
      </div>
    ) : null;
  };

  return (<div>
      <AppNavbar/>
      <Container>
        {renderError()}
        {title}
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label for="name">Name</Label>
            <Input type="text" name="name" id="name" value={restaurant.name || ''}
                   onChange={handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="address">Address</Label>
            <Input type="text" name="address" id="address" value={restaurant.address || ''}
                   onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/restaurants">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  )
};

export default RestaurantEdit;
