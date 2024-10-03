import {useHistory} from 'react-router-dom';

const useNavigateTo = () => {

    const history = useHistory();
    return (path, state = {}) => {
        history.push(path, state);
    };
};

export default useNavigateTo;
