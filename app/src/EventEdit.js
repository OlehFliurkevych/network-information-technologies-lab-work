import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { useCookies } from 'react-cookie';

const EventEdit = () => {
  const initialFormState = {
    restaurant: '',
    table: '',
    date: ''
  };
  const [bookEvent, setBookEvent, authenticated, setAuthenticated,
  loading, setLoading] = useState(initialFormState);
  const navigate = useNavigate();
  const [restaurants, setRestaurants] = useState([]);
  const [tables, setTables] = useState([]);
  const [restaurant, setRestaurant] = useState([]);
  const [table, setTable] = useState([]);
  const [user, setUser] = useState(undefined);
  const { id } = useParams();
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('/api/restaurants')
      .then(response => response.json())
      .then(data => {
        setRestaurants(data);
        setLoading(false);
      })

    fetch('/api/user', { credentials: 'include' }) // <.>
            .then(response => response.text())
            .then(body => {
              if (body === '') {
                setAuthenticated(false);
              } else {
                setUser(JSON.parse(body));
                setAuthenticated(true);
              }
              setLoading(false);
            });

    if (id !== 'new') {
      fetch(`/api/events/${id}`)
        .then(response => response.json())
        .then(data => setBookEvent(data));
    }
  }, [id, setBookEvent, setAuthenticated, setLoading, setUser, setRestaurants]);

  const handleChange = (event) => {
  console.log(event)
    const { name, value } = event.target

//    fetch(`/api/tables?restaurantId=${restaurantId}`)
//          .then(response => response.json())
//          .then(data => {
//            setTables(data);
//            setLoading(false);
//          })
//
//    setTables()

//    setTables()
    setBookEvent({ ...bookEvent, [name]: value })
  }

  const handleRestaurantChange = (event) => {
    const { name, value } = event.target
    console.log(event.target);
    console.log(event.target.value);

    const restaurantId = event.target.value;

    fetch(`/api/tables?restaurantId=${restaurantId}`)
              .then(response => response.json())
              .then(data => {
                setTables(data);
              })

    setRestaurant({ ...restaurant, [name]: value })
    setBookEvent({ ...bookEvent, [name]: value })
  }

  const handleTableChange = (event) => {
    const { name, value } = event.target

    setTable({ ...table, [name]: value })
  }

  const fetchTables = async (restaurantId) => {
    await fetch(`/api/tables?restaurantId=${restaurantId}`, {
      method: 'GET',
      headers: {
        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    }).then(() => {
      let updatedTables = [...tables].filter(i => i.id !== id);
      setTables(updatedTables);
    });
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`/api/events${bookEvent.id ? `/${bookEvent.id}` : ''}`, {
                             method: (bookEvent.id) ? 'PUT' : 'POST',
                             headers: {
                               'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
                               'Accept': 'application/json',
                               'Content-Type': 'application/json'
                             },
                             body: JSON.stringify(bookEvent),
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
      setBookEvent(initialFormState);
      navigate("/events");
    } catch (error) {
      setError(error.message);
      console.log(error);
      return error;
    }

}




//    await fetch(`/api/events${bookEvent.id ? `/${bookEvent.id}` : ''}`, {
//      method: (bookEvent.id) ? 'PUT' : 'POST',
//      headers: {
//        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
//        'Accept': 'application/json',
//        'Content-Type': 'application/json'
//      },
//      body: JSON.stringify(bookEvent),
//      credentials: 'include'
//    });
//    setBookEvent(initialFormState);
//    navigate("/events");
//  }

  const title = <h2>{bookEvent.id ? 'Edit booking event' : 'Add booking event'}</h2>;

    const renderError = () => {
      return error ? (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      ) : null;
    };

  return (
  <div>
        <AppNavbar/>
        <Container>
        {renderError()}
          {title}
          <Form onSubmit={handleSubmit}>
            <FormGroup>
              <Label for="restaurant">Restaurant</Label>
              <Input type="select" name="restaurant" id="restaurant" value={bookEvent.restaurant || ''}
                     onChange={handleRestaurantChange} autoComplete="name">
                {restaurants.map((restaurant) => (
                  <option key={restaurant.id} value={restaurant.id}>
                    {restaurant.name}
                  </option>
                ))}
              </Input>
            </FormGroup>
            <FormGroup>
              <Label for="table">Table</Label>
              <Input type="select" name="table" id="table" value={bookEvent.table || ''}
                     onChange={handleChange} autoComplete="name">
                {tables.map((table) => (
                  <option key={table.id} value={table.id}>
                    {table.tableType}
                  </option>
                ))}
              </Input>
            </FormGroup>
            <FormGroup>
              <Label for="date">Date</Label>
              <Input type="date" name="date" id="date" value={bookEvent.date || ''}
                     onChange={handleChange} autoComplete="name"/>
            </FormGroup>
            <FormGroup>
              <Label for="time">Time</Label>
              <Input type="time" name="time" id="time" value={bookEvent.time || ''}
                     onChange={handleChange} autoComplete="name"/>
            </FormGroup>
            <FormGroup>
              <Button color="primary" type="submit">Save</Button>{' '}
              <Button color="secondary" tag={Link} to={"/events"}>Cancel</Button>
            </FormGroup>
          </Form>
        </Container>
      </div>
  )
};

export default EventEdit;