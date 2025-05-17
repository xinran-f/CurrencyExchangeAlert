import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';

export default function App() {
  const [alerts, setAlerts] = useState([]);
  const [form, setForm] = useState({ userId: '', baseCurrency: 'USD', targetCurrency: 'EUR', targetRate: '' });
  const [rateLookup, setRateLookup] = useState({ base: 'USD', target: 'EUR' });
  const [currentRate, setCurrentRate] = useState(null);
  const [message, setMessage] = useState(null);
  const [messageType, setMessageType] = useState('');

  // Show temporary messages
  const showMessage = (msg, type = 'success') => {
    setMessage(msg);
    setMessageType(type);
    setTimeout(() => setMessage(null), 3000);
  };

  // Load existing alerts (wrapped in useCallback)
  const loadAlerts = useCallback(async () => {
    try {
      const res = await axios.get('/api/alerts');
      setAlerts(res.data);
    } catch (err) {
      console.error('Error loading alerts', err);
      showMessage('Error loading alerts', 'error');
    }
  }, []);

  // Run once on mount
  useEffect(() => {
    loadAlerts();
  }, [loadAlerts]);

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const submitAlert = async e => {
    e.preventDefault();
    try {
      await axios.post('/api/alerts', {
        userId: Number(form.userId),
        baseCurrency: form.baseCurrency,
        targetCurrency: form.targetCurrency,
        targetRate: parseFloat(form.targetRate)
      });
      setForm({ ...form, targetRate: '' });
      showMessage('Alert created successfully', 'success');
      loadAlerts();
    } catch (err) {
      console.error('Error creating alert', err);
      showMessage('Error creating alert', 'error');
    }
  };

  const deleteAlert = async id => {
    try {
      await axios.delete(`/api/alerts/${id}`);
      showMessage('Alert deleted', 'success');
      loadAlerts();
    } catch (err) {
      console.error('Error deleting alert', err);
      showMessage('Error deleting alert', 'error');
    }
  };

  const lookupRate = async () => {
    try {
      const res = await axios.get('/api/rate', { params: rateLookup });
      setCurrentRate(res.data);
      showMessage(`Current rate: ${res.data}`, 'success');
    } catch (err) {
      console.error('Error fetching rate', err);
      showMessage('Error fetching rate', 'error');
    }
  };

  return (
      <div className="p-4 max-w-xl mx-auto">
        <h1 className="text-2xl mb-4">Exchange Rate Alerts</h1>

        {message && (
            <div className={`p-2 mb-4 rounded ${
                messageType === 'success'
                    ? 'bg-green-200 text-green-800'
                    : 'bg-red-200 text-red-800'
            }`}> {message} </div>
        )}

        <form onSubmit={submitAlert} className="mb-6">
          <div className="mb-2">
            <label className="block">User ID:</label>
            <input
                type="number"
                name="userId"
                value={form.userId}
                onChange={handleFormChange}
                required
                className="border p-1 w-full"
            />
          </div>
          <div className="mb-2">
            <label className="block">Base Currency:</label>
            <input
                type="text"
                name="baseCurrency"
                value={form.baseCurrency}
                onChange={handleFormChange}
                className="border p-1 w-full"
            />
          </div>
          <div className="mb-2">
            <label className="block">Target Currency:</label>
            <input
                type="text"
                name="targetCurrency"
                value={form.targetCurrency}
                onChange={handleFormChange}
                className="border p-1 w-full"
            />
          </div>
          <div className="mb-2">
            <label className="block">Target Rate:</label>
            <input
                type="number"
                step="0.0001"
                name="targetRate"
                value={form.targetRate}
                onChange={handleFormChange}
                required
                className="border p-1 w-full"
            />
          </div>
          <button
              type="submit"
              className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            Create Alert
          </button>
        </form>

        <h2 className="text-xl mb-2">Active Alerts</h2>
        <ul className="mb-6">
          {alerts.map(a => (
              <li
                  key={a.id}
                  className="flex justify-between items-center mb-1"
              >
            <span>
              {`#${a.id} User:${a.userId} ${a.baseCurrency}→${a.targetCurrency} @ ${a.targetRate}`}
            </span>
                <button
                    onClick={() => deleteAlert(a.id)}
                    className="text-red-500"
                >
                  Delete
                </button>
              </li>
          ))}
        </ul>

        <h2 className="text-xl mb-2">Lookup Rate</h2>
        <div className="flex mb-2">
          <input
              type="text"
              name="base"
              value={rateLookup.base}
              onChange={e => setRateLookup({ ...rateLookup, base: e.target.value })}
              className="border p-1 mr-2 flex-1"
          />
          <input
              type="text"
              name="target"
              value={rateLookup.target}
              onChange={e => setRateLookup({ ...rateLookup, target: e.target.value })}
              className="border p-1 mr-2 flex-1"
          />
          <button
              onClick={lookupRate}
              className="bg-green-500 text-white px-3 rounded"
          >
            Check
          </button>
        </div>
        {currentRate !== null && <div>Current Rate: {currentRate}</div>}
      </div>
  );
}