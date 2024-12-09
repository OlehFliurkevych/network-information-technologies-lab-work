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
//  const {restaurantId} = useParams();

  useEffect(() => {
    setLoading(true);



      console.log(location);
      const queryParams = new URLSearchParams(location.search);
      console.log(queryParams);
      const restaurantId = queryParams.get('restaurantId');

    fetch(`/api/tables?restaurantId=${restaurantId}`)
      .then(response => response.json())
      .then(data => {
        setTables(data);
        setLoading(false);
      })
  }, [location.search]);

  const remove = async (id) => {
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

  const tableList = tables.map(table => {
   return <tr key={table.id}>
      <td style={{ whiteSpace: 'nowrap' }}>{table.tableType}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{table.count}</td>
      <td>
        <ButtonGroup>
          <Button size="sm" color="primary" tag={Link} to={"/tables/" + table.id}>Edit</Button>
          <Button size="sm" color="danger" onClick={() => remove(table.id)}>Delete</Button>
          <Button size="sm" color="success" tag={Link} to={"/restaurants/" + table.id + "/tables/"}>View tables</Button>
        </ButtonGroup>
      </td>
    </tr>
  });

   const message = <h3>Manage your tables!</h3>;

  return (
    <div>
      <AppNavbar/>
      <Container fluid>
        <div className="float-end">
          <Button color="success" tag={Link} to="/tables/new">Add Table</Button>
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
        <h3>Return to restaurants</h3>
      </Container>
    </div>
  );
};

export default TableList;
