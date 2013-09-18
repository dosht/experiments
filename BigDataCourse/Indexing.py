'''
Created on Sep 7, 2013

@author: msameh
'''
docs = {1: "the quick brown fox jumps over the lazy dog",
        2: "a bird in a hand worth tow in a bush",
        3: "the lazy bird misses the worm"}

inv_index = {}

def index():
    for _id, document in docs.iteritems():
        for term in document.split(" "):
            item = inv_index.get(term) or []
            if _id not in item:
                item.append(_id)
            inv_index[term] = item

def search1(q):
    terms = q.split(" ")
    result = []
    for term in terms:
        res = inv_index.get(term)
        if res:
            result.append(res)
    return result

def search2(q):
    raw_result = search1(q)
    scored_result = {}
    for item in raw_result:
        for _id in item:
            scored_result[_id] = 1 + (scored_result.get(_id) or 0)
    return [(k, v) for (k, v) in scored_result.iteritems()]

index()
#for i in inv_index.iteritems(): print i
print search1("the")
print search2("the")
