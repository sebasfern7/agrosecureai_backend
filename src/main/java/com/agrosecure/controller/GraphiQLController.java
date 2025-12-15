package com.agrosecure.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphiQLController {

    @GetMapping(value = "/graphiql")
    public ResponseEntity<String> graphiql() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>GraphiQL</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/graphiql@2/graphiql.min.css" />
            </head>
            <body style="margin: 0;">
                <div id="graphiql" style="height: 100vh;"></div>
                <script src="https://cdn.jsdelivr.net/npm/react@18/umd/react.production.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/react-dom@18/umd/react-dom.production.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/graphiql@2/graphiql.min.js"></script>
                <script>
                    function graphQLFetcher(graphQLParams) {
                        console.log('GraphQL Request:', graphQLParams);
                        return fetch('/graphql', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify(graphQLParams),
                        })
                        .then(function(response) {
                            console.log('Response status:', response.status);
                            console.log('Response headers:', response.headers);
                            if (!response.ok) {
                                return response.text().then(function(text) {
                                    console.error('HTTP error response:', text);
                                    return { errors: [{ message: 'HTTP ' + response.status + ': ' + text }] };
                                });
                            }
                            var contentType = response.headers.get('content-type');
                            console.log('Content-Type:', contentType);
                            if (contentType && contentType.includes('application/json')) {
                                return response.json().then(function(data) {
                                    console.log('GraphQL Response:', data);
                                    return data;
                                }).catch(function(e) {
                                    console.error('JSON parse error:', e);
                                    return response.text().then(function(text) {
                                        console.error('Response text:', text);
                                        return { errors: [{ message: 'Invalid JSON: ' + text.substring(0, 200) }] };
                                    });
                                });
                            } else {
                                return response.text().then(function(text) {
                                    console.error('Non-JSON response:', text);
                                    return { errors: [{ message: 'Expected JSON but got: ' + contentType + ' - ' + text.substring(0, 200) }] };
                                });
                            }
                        })
                        .catch(function(error) {
                            console.error('Fetch error:', error);
                            return { errors: [{ message: 'Network error: ' + error.message }] };
                        });
                    }
                    ReactDOM.render(
                        React.createElement(GraphiQL, { fetcher: graphQLFetcher }),
                        document.getElementById('graphiql')
                    );
                </script>
            </body>
            </html>
            """;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }
}

