import { ApolloClient, InMemoryCache, HttpLink } from '@apollo/client';

// Usamos el proxy de Vite para evitar CORS
const GRAPHQL_URL = '/graphql';

export const apolloClient = new ApolloClient({
  link: new HttpLink({
    uri: GRAPHQL_URL,
  }),
  cache: new InMemoryCache(),
});
