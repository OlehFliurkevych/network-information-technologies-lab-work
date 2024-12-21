import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams, useLocation } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { useCookies } from 'react-cookie';

const TableEdit = () => {
  const initialFormState = {
    tableType: '',
    count: '',
  };
  const [table, setTable] = useState(initialFormState);
  const [tableType, setTableType] = useState(initialFormState);
  const navigate = useNavigate();
  const { id } = useParams();
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const restaurantId = queryParams.get('restaurantId');
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id !== 'new') {
      fetch(`/api/tables/${id}?restaurantId=${restaurantId}`)
        .then(handleError)
        .then(response => response.json())
        .then(data => setTable(data))
        .catch(error => setError(error.toString()));
    }
  }, [id, setTable]);

   const tableTypes = [
     { code: 'TWO_PERSON', desc: 'Table for at least 2 person and max 4 person' },
     { code: 'FOUR_PERSON', desc: 'Table for at least 4 person and max 8 person' },
     { code: 'EIGHT_PERSON', desc: 'Table for at least 8 person and max 12 person' },
   ];

  const handleChange = (event) => {
    const { name, value } = event.target

    setTable({ ...table, [name]: value })
  }

  const handleTableType = (event) => {
    console.log(event.target)
    console.log(event.target.value)
    const { name, value } = event.target
    const valueType = event.target.value;
    console.log(valueType);

    setTableType({ ...tableType, [name]: value })
  }

  const handleError = (response) => {
    if (!response.ok) {
      return response.json().then(err => {
        throw new Error(`${err.errorType}: ${err.errorMessage}`);
      });
    }
    return response.json();
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    console.log("Handle submit before")
    console.log(tableType)
    console.log("Handle submit after")

    try {
      const response = await fetch(`/api/tables${table.id ? `/${table.id}?restaurantId=${restaurantId}` : `?restaurantId=${restaurantId}`}`, {
                                            method: (table.id) ? 'PUT' : 'POST',
                                            headers: {
                                              'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
                                              'Accept': 'application/json',
                                              'Content-Type': 'application/json'
                                            },
                                            body: JSON.stringify(table),
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
      setTable(initialFormState);
      console.log("Try block before navigate");
      navigate('/tables?restaurantId=' + restaurantId);
    } catch (error) {
      setError(error.message);
      console.log(error);
      return error;
    }

//      try {
//        console.log("Try block before fetch");
//        await fetch(`/api/tables${table.id ? `/${table.id}?restaurantId=${restaurantId}` : `?restaurantId=${restaurantId}`}`, {
//                     method: (table.id) ? 'PUT' : 'POST',
//                     headers: {
//                       'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
//                       'Accept': 'application/json',
//                       'Content-Type': 'application/json'
//                     },
//                     body: JSON.stringify(table),
//                     credentials: 'include'
//                   });
//        console.log("Try block after fetch");
//        setTable(initialFormState);
//        console.log("Try block before navigate");
//        navigate('/tables?restaurantId=' + restaurantId);
//      } catch (error) {
//        setError(err.message);
//        console.log("Catch block before error");
//        console.error(error); // You might send an exception to your error tracker like AppSignal
//        return error;
//      }

//    await fetch(`/api/tables${table.id ? `/${table.id}?restaurantId=${restaurantId}` : `?restaurantId=${restaurantId}`}`, {
//      method: (table.id) ? 'PUT' : 'POST',
//      headers: {
//        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
//        'Accept': 'application/json',
//        'Content-Type': 'application/json'
//      },
//      body: JSON.stringify(table),
//      credentials: 'include'
//    }).catch(error => setError(error.toString()));

  }

  const title = <h2>{table.id ? 'Edit table' : 'Add table'}</h2>;

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
              <Label for="tableType">Table type</Label>
              <Input type="select" name="tableType" id="tableType" value={table.tableType || ''}
                     onChange={handleChange} autoComplete="name">
               {tableTypes.map((tableType) => (
                                 <option key={tableType.code} value={tableType.code}>
                                   {tableType.desc}
                                 </option>
               ))}
              </Input>
            </FormGroup>
          <FormGroup>
            <Label for="count">Count of tables</Label>
            <Input type="text" name="count" id="count" value={table.count || ''}
                   onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Save</Button>{' '}
            <Button color="secondary" tag={Link} to={"/tables?restaurantId=" + restaurantId}>Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  )
};

export default TableEdit;
