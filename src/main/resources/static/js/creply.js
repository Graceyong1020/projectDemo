async function get1(cno) {

    const result = await axios.get(`/creplies/clist/${cno}`)
    //console.log(result)
    return result;
}

async function getList({cno, page, size, goLast}){
    console.log("cno: " + cno + page+size)

    const result = await axios.get(`/creplies/clist/${cno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({cno:cno, page:lastPage, size:size})

    }

    return result.data
}

async function addReply(replyObj) {
    if (!replyObj.cno) {
        console.error('cno is required');
        throw new Error('cno is required');
    }
    try {
        const response = await axios.post(`/creplies/`, replyObj);
        return response.data;
    } catch (error) {
        console.error('Error adding reply:', error.response ? error.response.data : error.message);
        throw error;
    }
}

async function getReply(rno) {
    const response = await axios.get(`/creplies/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/creplies/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/creplies/${rno}`)
    return response.data
}