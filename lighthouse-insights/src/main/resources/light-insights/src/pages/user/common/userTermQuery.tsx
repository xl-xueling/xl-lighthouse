import React, {useEffect, useRef, useState} from 'react';
import {
    Select,
} from '@arco-design/web-react';
import { useCallback } from 'react';
import { Spin } from '@arco-design/web-react';
import debounce from 'lodash/debounce';
import {IconUser} from "@arco-design/web-react/icon";
import {requestQueryByIds} from "@/api/user";
import {User} from "@/types/insights-web";

const UserTermQuery = ({initValues = null,completeCallBack=null}) => {

    const [initData,setInitData] = useState<Array<User>>(null);

    const fetchInitData = async () => {
        if(initValues){
            const result = await requestQueryByIds({"ids":initValues})
            setInitData(result.data.list);
            const options = result.data.list.map((user) => (
                {
                label: (
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                        <IconUser style={{ marginRight:5 }}/>
                        {user.userName}
                    </div>
                ),
                value: user.id,
                checked:true,
                selected:true,
            }));
            setFetching(false);
            setOptions(options);
            setValues(["1","2"]);
            setTimeout(()=>{
                completeCallBack();
            },1000);
        }
    }

    useEffect(() => {
        setValues([]);
        fetchInitData().then();
    },[initValues])


    function instanceFetchData() {
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
                            <div style={{display: 'flex', alignItems: 'center'}}>
                                <IconUser style={{marginRight: 5}}/>
                                {`${user.name.first} ${user.name.last}`}
                            </div>
                        ),
                        value: user.email,
                    }));
                    setFetching(false);
                    setOptions(options);
                }
            });
    }


    const [options, setOptions] = useState([]);
    const [values, setValues] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef(null);
    const debouncedFetchUser = useCallback(
        debounce((inputValue) => {
            // refFetchId.current = Date.now();
            // const fetchId = refFetchId.current;
            // setFetching(true);
            // setOptions([]);
            // fetch('https://randomuser.me/api/?results=5')
            //     .then((response) => response.json())
            //     .then((body) => {
            //         if (refFetchId.current === fetchId) {
            //             const options = body.results.map((user) => ({
            //                 label: (
            //                     <div style={{ display: 'flex', alignItems: 'center' }}>
            //                         <IconUser style={{ marginRight:5 }}/>
            //                         {`${user.name.first} ${user.name.last}`}
            //                     </div>
            //                 ),
            //                 value: user.email,
            //             }));
            //             setFetching(false);
            //             setOptions(options);
            //         }
            //     });
            instanceFetchData()
        }, 500),
        []
    );

    return (
        <Select
            showSearch
            mode='multiple'
            options={options}
            maxTagCount={5}
           // value={["1","2"]}
            value={values}
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