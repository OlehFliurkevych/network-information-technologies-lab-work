import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link, useParams, useLocation } from 'react-router-dom'
import { useCookies } from 'react-cookie';

const TableList = () => {

  const [authenticated, setAuthenticated] = useState(false);
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState(undefined);
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const restaurantId = queryParams.get('restaurantId');
  const [error, setError] = useState(null);
  //const {restaurantId} = useParams();
  const tableTypes = [
       { code: "TWO_PERSON", desc: "Table for at least 2 person and max 4 person" },
       { code: "FOUR_PERSON", desc: "Table for at least 4 person and max 8 person" },
       { code: "EIGHT_PERSON", desc: "Table for at least 8 person and max 12 person" },
     ];

  useEffect(() => {
    setLoading(true);

    fetch(`/api/tables?restaurantId=${restaurantId}`)
      .then(response => response.json())
      .then(data => {
        setTables(data);
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
  }, [location.search, setUser]);

  const remove = async (id) => {
    try {
            const response = await fetch(`/api/tables/${id}`, {
                                   method: 'DELETE',
                                   headers: {
                                     'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
                                     'Accept': 'application/json',
                                     'Content-Type': 'application/json'
                                   },
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
          response.then(() => {
                         let updatedTables = [...tables].filter(i => i.id !== id);
                         setTables(updatedTables);
                       });
          } catch (error){
                setError(error.message);
                console.log(error);
                return error;
          }
    await fetch(`/api/tables/${id}`, {
      method: 'DELETE',
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

  if (loading) {
    return <p>Loading...</p>;
  }

  function search(nameKey, myArray){
      for (let i=0; i < myArray.length; i++) {
          if (myArray[i].code === nameKey) {
              return myArray[i];
          }
      }
  }

  const tableList = tables.map(table => {
//   const tableTypeDesc = search(table.tableType, tableTypes);
//   console.log(tableTypeDesc);
   return <tr key={table.id}>
      <td style={{ whiteSpace: 'nowrap' }}>{table.tableType}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{table.count}</td>
      <td>
        <ButtonGroup>
          <Button size="sm" color="primary" tag={Link} to={"/tables/" + table.id + "?restaurantId=" + restaurantId}>Edit</Button>
          <Button size="sm" color="danger" onClick={() => remove(table.id)}>Delete</Button>
        </ButtonGroup>
      </td>
    </tr>
  });

    const message = user ?
          <h4>Manage your tables, {user.name}!</h4> :
          <p>Please log in to manage your tables.</p>;

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
      <Container fluid>
      {renderError()}
        <div className="float-end">
        </div>
        <h3>{message}</h3>
        <Table className="mt-4">
          <thead>
          <tr>
            <th width="20%">Table type</th>
            <th width="20%">Count of tables</th>
            <th width="10%">Actions</th>
          </tr>
          </thead>
          <tbody>
          {tableList}
          </tbody>
        </Table>
        <ButtonGroup>
          <Button size="sm" color="success" tag={Link} to={"/tables/new?restaurantId=" + restaurantId}>Add Table</Button>
          <Button size="sm" color="warning" tag={Link} to="/restaurants">Return to restaurants</Button>
          <Button size="sm" color="primary" tag={Link} to="/events">Book table</Button>
        </ButtonGroup>
      </Container>
    </div>
  );
};

export default TableList;
