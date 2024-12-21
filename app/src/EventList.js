import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link, useParams, useLocation } from 'react-router-dom'
import { useCookies } from 'react-cookie';

const EventList = () => {

  const [authenticated, setAuthenticated] = useState(false);
  const [bookEvents, setBookEvents, restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState(undefined);
  const [cookies] = useCookies(['XSRF-TOKEN']);

  useEffect(() => {
    setLoading(true);

    fetch('/api/events')
      .then(response => response.json())
      .then(data => {
        setBookEvents(data);
        setLoading(false);
      })

    fetch('api/user', { credentials: 'include' }) // <.>
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

  }, [setAuthenticated, setLoading, setUser]);

  const remove = async (id) => {
    await fetch(`/api/events/${id}`, {
      method: 'DELETE',
      headers: {
        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    }).then(() => {
      let updatedBookEvents = [...bookEvents].filter(i => i.id !== id);
      setBookEvents(updatedBookEvents);
    });
  }

  if (loading) {
    return <p>Loading...</p>;
  }

  const message = user ?
      <h4>Manage your bookings, {user.name}!</h4> :
      <p>Please log in to manage your restaurants.</p>;

  const eventList = bookEvents.map(bookEvent => {
  console.log(bookEvent);
   return <tr key={bookEvent.id}>
      <td style={{ whiteSpace: 'nowrap' }}>{bookEvent.restaurant.name}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{bookEvent.restaurant.address}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{bookEvent.table.tableType}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{bookEvent.date}</td>
      <td>
        <ButtonGroup>
          <Button size="sm" color="primary" tag={Link} to={"/events/" + bookEvent.id}>Edit</Button>
          <Button size="sm" color="danger" onClick={() => remove(bookEvent.id)}>Delete</Button>
        </ButtonGroup>
      </td>
    </tr>
  });

  return (
    <div>
      <AppNavbar/>
      <Container fluid>
        <h3>{message}</h3>
        <Table className="mt-4">
          <thead>
          <tr>
            <th width="20%">Restaurant</th>
            <th width="20%">Address</th>
            <th width="20%">Table type</th>
            <th width="20%">Date</th>
            <th width="20%">Operations</th>
          </tr>
          </thead>
          <tbody>
          {eventList}
          </tbody>
        </Table>
        <ButtonGroup>
          <Button size="sm" color="warning" tag={Link} to="/restaurants">Return to restaurants</Button>
          <Button size="sm" color="success" tag={Link} to="/events/new">Add Event</Button>
        </ButtonGroup>
      </Container>
    </div>
  );
};

export default EventList;
