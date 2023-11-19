import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Select,
} from '@arco-design/web-react';
import { useCallback } from 'react';
import { Spin, Avatar } from '@arco-design/web-react';
import debounce from 'lodash/debounce';

function UserTermQuery() {
    const [options, setOptions] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef(null);
    const debouncedFetchUser = useCallback(
        debounce((inputValue) => {
            refFetchId.current = Date.now();
            const fetchId = refFetchId.current;
            setFetching(true);
            setOptions([]);
            fetch('https://randomuser.me/api/?results=5')
                .then((response) => response.json())
                .then((body) => {
                    if (refFetchId.current === fetchId) {
                        const options = body.results.map((user) => ({
                            label: (
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <Avatar size={24} style={{ marginLeft: 6, marginRight: 12 }}>
                                        <img alt='avatar' src={user.picture.thumbnail} />
                                    </Avatar>
                                    {`${user.name.first} ${user.name.last}`}
                                </div>
                            ),
                            value: user.email,
                        }));
                        setFetching(false);
                        setOptions(options);
                    }
                });
        }, 500),
        []
    );

    return (
        <Select
            showSearch
            mode='multiple'
            options={options}
            placeholder='Search by name'
            filterOption={false}
            notFoundContent={
                fetching ? (
                    <div
                        style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                        }}
                    >
                        <Spin style={{ margin: 12 }} />
                    </div>
                ) : null
            }
            onSearch={debouncedFetchUser}
        />
    );
}

export default UserTermQuery;