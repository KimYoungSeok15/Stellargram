import sqlite3
f = open('hyg_v37.csv')

lines = f.readlines()
header = lines[0].rstrip().split(',')
for idx, element in enumerate(header):
    if element == 'var':
        header[idx] = 'variable'

conn = sqlite3.connect('./StarData.db')
cur = conn.cursor()

table_name = 'stars'

delete_query = f"DROP TABLE IF EXISTS {table_name}"

cur.execute(delete_query)
conn.commit()



lis = []
for idx, ele in enumerate(header):
    if ele in ['hip', 'hp', 'hr', 'hd', 'comp', 'comp_primary', 'flam']:
        lis.append(f'{ele} INTEGER NULL')
    elif ele in ['ra', 'dec', 'dist', 'pmra', 'pmdec', 'rv', 'mag', 'absmag', 'ci', 'x', 'y', 'z', 'vx', 'vy', 'vz', 'rarad', 'decrad', 'pmrarad', 'pmdecrad', 'lum', 'var_min', 'var_max']:
        lis.append(f'{ele} REAL NULL')
    elif ele == 'id':
        lis.append(f'{ele} INTEGER NOT NULL PRIMARY KEY')
    else:
        lis.append(f'{ele} TEXT NULL')
create_query_body = f'({", ".join(lis)})'
create_query = f"CREATE TABLE {table_name} {create_query_body}"
cur.execute(create_query)
conn.commit()

query = []
for line in lines[1:]:
    eles = line.rstrip().split(',')
    llis = []
    for idx, ele in enumerate(eles):
        if header[idx] in ['id', 'hip','hd', 'hp', 'hr', 'comp', 'comp_primary', 'flam']:
            llis.append(int(ele) if ele else None)
        elif header[idx] in ['ra', 'dec', 'dist', 'pmra', 'pmdec', 'rv', 'mag', 'absmag', 'ci', 'x', 'y', 'z', 'vx', 'vy', 'vz', 'rarad', 'decrad', 'pmrarad', 'pmdecrad', 'lum', 'var_min', 'var_max']:
            llis.append(float(ele) if ele else None)
        else:
            llis.append(ele)
    query.append(tuple(llis))
insert_many = f"INSERT INTO {table_name}({','.join(header)}) VALUES({','.join(['?'] * len(header))})"
print(insert_many)
cur.executemany(insert_many, query)
conn.commit()