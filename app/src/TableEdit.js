import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { useCookies } from 'react-cookie';

const TableEdit = () => {
  const initialFormState = {
    name: '',
    address: '',
  };
  const [table, setTable] = useState(initialFormState);
  const navigate = useNavigate();
  const { id } = useParams();
  const [cookies] = useCookies(['XSRF-TOKEN']);

  useEffect(() => {
    if (id !== 'new') {
      fetch(`/api/tables/${id}`)
        .then(response => response.json())
        .then(data => setTable(data));
    }
  }, [id, setTable]);

  const handleChange = (event) => {
    const { name, value } = event.target

    setTable({ ...table, [name]: value })
  }

  const handleSubmit = async (event) => {
    event.preventDefault();

    await fetch(`/api/tables${table.id ? `/${table.id}` : ''}`, {
      method: (table.id) ? 'PUT' : 'POST',
      headers: {
        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(table),
      credentials: 'include'
    });
    setTable(initialFormState);
    navigate('/tables');
  }

  const title = <h2>{table.id ? 'Edit table' : 'Add table'}</h2>;

  return (<div>
      <AppNavbar/>
      <Container>
        {title}
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label for="tableType">Table type</Label>
            <Input type="text" name="tableType" id="tableType" value={table.tableType || ''}
                   onChange={handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="count">Count of tables</Label>
            <Input type="text" name="count" id="count" value={table.count || ''}
                   onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/tables">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  )
};

export default TableEdit;
