/* eslint-disable @typescript-eslint/no-explicit-any */
import apiClient from './client';
import { GolfBall } from '@/types/golfball';

export const golfBallService = {
  getAll: async (): Promise<GolfBall[]> => {
    const response = await apiClient.get('/golf-balls');
    return response.data;
  },

  getById: async (id: string): Promise<GolfBall> => {
    const response = await apiClient.get(`/golf-balls/${id}`);
    return response.data;
  },

  create: async (data: any): Promise<GolfBall> => {
    const response = await apiClient.post('/golf-balls', data);
    return response.data;
  },

  update: async (id: string, data: any): Promise<GolfBall> => {
    const response = await apiClient.put(`/golf-balls/${id}`, data);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/golf-balls/${id}`);
  },
};

export const customerService = {
  getAll: async () => {
    const response = await apiClient.get('/customers');
    return response.data;
  },

  getById: async (id: string) => {
    const response = await apiClient.get(`/customers/${id}`);
    return response.data;
  },

  create: async (data: any) => {
    const response = await apiClient.post('/customers', data);
    return response.data;
  },

  update: async (id: string, data: any) => {
    const response = await apiClient.put(`/customers/${id}`, data);
    return response.data;
  },

  delete: async (id: string) => {
    await apiClient.delete(`/customers/${id}`);
  },
};

export const orderService = {
  getAll: async () => {
    const response = await apiClient.get('/orders');
    return response.data;
  },

  getById: async (id: string) => {
    const response = await apiClient.get(`/orders/${id}`);
    return response.data;
  },

  create: async (data: any) => {
    const response = await apiClient.post('/orders', data);
    return response.data;
  },

  updateStatus: async (id: string, status: string) => {
    const response = await apiClient.patch(`/orders/${id}/status`, { status });
    return response.data;
  },

  cancel: async (id: string) => {
    const response = await apiClient.post(`/orders/${id}/cancel`);
    return response.data;
  },
};
