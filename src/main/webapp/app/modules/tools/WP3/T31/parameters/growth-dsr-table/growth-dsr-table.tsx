import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Input, Table } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';

const GrowthDSRTable = (props: any) => {
  const { title, section } = props;

  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  const convertStringToNumber = (value, section, profile, year) => {
    if (typeof value === 'string' && value !== '') {
      return parseFloat(value);
    } else if (value === '') {
      return defaultParameters[section]?.[profile]?.[year];
    }
    return value;
  };

  return (
    <>
      <span>{title}</span>
      <Table>
        <thead>
          <tr>
            <th>Type</th>
            <th>2020</th>
            <th>2030</th>
            <th>2040</th>
            <th>2050</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Active</td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Active][2020]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Active', '2020'),
                  })
                }
                error={errors?.parameters?.[section]?.Active?.['2020']}
                id={`${section}-active-2020`}
                name={`parameters[${section}][Active][2020]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Active][2030]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Active', '2030'),
                  })
                }
                error={errors?.parameters?.[section]?.Active?.['2030']}
                id={`${section}-active-2030`}
                name={`parameters[${section}][Active][2030]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Active][2040]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Active', '2040'),
                  })
                }
                error={errors?.parameters?.[section]?.Active?.['2040']}
                id={`${section}-active-2040`}
                name={`parameters[${section}][Active][2040]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Active][2050]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Active', '2050'),
                  })
                }
                error={errors?.parameters?.[section]?.Active?.['2050']}
                id={`${section}-active-2050`}
                name={`parameters[${section}][Active][2050]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
          </tr>
          <tr>
            <td>Slow</td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Slow][2020]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Slow', '2020'),
                  })
                }
                error={errors?.parameters?.[section]?.Slow?.['2020']}
                id={`${section}-slow-2020`}
                name={`parameters[${section}][Slow][2020]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Slow][2030]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Slow', '2030'),
                  })
                }
                error={errors?.parameters?.[section]?.Slow?.['2030']}
                id={`${section}-slow-2030`}
                name={`parameters[${section}][Slow][2030]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Slow][2040]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Slow', '2040'),
                  })
                }
                error={errors?.parameters?.[section]?.Slow?.['2040']}
                id={`${section}-slow-2040`}
                name={`parameters[${section}][Slow][2040]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
            <td>
              <ValidatedField
                register={() =>
                  register(`parameters[${section}][Slow][2050]`, {
                    setValueAs: value => convertStringToNumber(value, section, 'Slow', '2050'),
                  })
                }
                error={errors?.parameters?.[section]?.Slow?.['2050']}
                id={`${section}-slow-2050`}
                name={`parameters[${section}][Slow][2050]`}
                data-cy="growth"
                type="number"
                min={0}
                step="any"
              />
            </td>
          </tr>
        </tbody>
      </Table>
    </>
  );
};

export default GrowthDSRTable;
